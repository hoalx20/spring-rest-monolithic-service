package spring.iam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Failed;
import spring.iam.exception.ServiceExc;
import spring.iam.mapper.StatusMpr;
import spring.iam.model.Status;
import spring.iam.model.User;
import spring.iam.model.dto.StatusCre;
import spring.iam.model.dto.StatusRes;
import spring.iam.model.dto.StatusUpd;
import spring.iam.repository.StatusRepo;
import spring.iam.repository.UserRepo;
import spring.iam.response.Multi;
import spring.iam.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusSrv implements IStatusSrv {
	StatusRepo statusRepo;
	UserRepo userRepo;
	StatusMpr statusMpr;

	public Status ensureExistedById(Long id) {
		Optional<Status> old = statusRepo.findById(id);
		if (!old.isPresent()) {
			throw new ServiceExc(Failed.NOT_EXISTS);
		}
		return old.get();
	}

	public User ensureOwningExistedByIds(Long id) {
		Optional<User> owning = userRepo.findById(id);
		if (!owning.isPresent())
			throw new ServiceExc(Failed.OWNING_SIDE_NOT_EXISTS);
		return owning.get();
	}

	public void ensureOwningAvailableById(Long id) {
		Optional<Status> existed = statusRepo.findByUserId(id);
		if (existed.isPresent()) {
			throw new ServiceExc(Failed.OWNING_SIDE_NOT_AVAILABLE);
		}
	}

	@Override
	public StatusRes save(StatusCre creation) {
		try {
			Status model = statusMpr.asModel(creation);
			var owning = ensureOwningExistedByIds(creation.getUserId());
			ensureOwningAvailableById(creation.getUserId());
			model.setUser(owning);
			Status saved = statusRepo.save(model);
			return statusMpr.asResponse(saved);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SAVE);
		}
	}

	@Override
	public StatusRes findById(Long id) {
		try {
			Optional<Status> queried = statusRepo.findById(id);
			if (!queried.isPresent())
				throw new ServiceExc(Failed.FIND_BY_ID_NO_CONTENT);
			return statusMpr.asResponse(queried.get());
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_BY_ID);
		}
	}

	@Override
	public Multi<StatusRes> findAll(int page, int size) {
		try {
			var pageable = statusRepo.findAll(PageRequest.of(page, size));
			List<StatusRes> values = statusMpr.asListResponse(pageable.getContent());
			if (CollectionUtils.isEmpty(values))
				throw new ServiceExc(Failed.FIND_ALL_NO_CONTENT);
			Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
			return new Multi<>(values, paging);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_ALL);
		}
	}

	@Override
	public Multi<StatusRes> findAllArchived(int page, int size) {
		try {
			var pageable = statusRepo.findAllByDeletedTrue(PageRequest.of(page, size));
			List<StatusRes> values = statusMpr.asListResponse(pageable.getContent());
			if (CollectionUtils.isEmpty(values))
				throw new ServiceExc(Failed.FIND_ALL_NO_CONTENT);
			Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
			return new Multi<>(values, paging);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_ALL);
		}
	}

	@Override
	public StatusRes updateById(Long id, StatusUpd update) {
		try {
			var value = ensureExistedById(id);
			statusMpr.mergeModel(value, update);
			Status model = statusRepo.save(value);
			return statusMpr.asResponse(model);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.UPDATE);
		}
	}

	@Override
	public StatusRes deleteById(Long id) {
		try {
			var value = ensureExistedById(id);
			StatusRes response = statusMpr.asResponse(value);
			statusRepo.delete(value);
			return response;
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.DELETE);
		}
	}
}
