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
import spring.iam.mapper.DeviceMpr;
import spring.iam.model.Device;
import spring.iam.model.User;
import spring.iam.model.dto.DeviceCre;
import spring.iam.model.dto.DeviceRes;
import spring.iam.model.dto.DeviceUpd;
import spring.iam.repository.DeviceRepo;
import spring.iam.repository.UserRepo;
import spring.iam.response.Multi;
import spring.iam.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceSrv implements IDeviceSrv {
	DeviceRepo deviceRepo;
	UserRepo userRepo;
	DeviceMpr deviceMpr;

	@Override
	public Device ensureExistedById(Long id) {
		Optional<Device> old = deviceRepo.findById(id);
		if (!old.isPresent()) {
			throw new ServiceExc(Failed.NOT_EXISTS);
		}
		return old.get();
	}

	@Override
	public User ensureOwningExistedByIds(Long id) {
		Optional<User> owning = userRepo.findById(id);
		if (!owning.isPresent())
			throw new ServiceExc(Failed.OWNING_SIDE_NOT_EXISTS);
		return owning.get();
	}

	@Override
	public DeviceRes save(DeviceCre creation) {
		try {
			Device model = deviceMpr.asModel(creation);
			var owning = ensureOwningExistedByIds(creation.getUserId());
			model.setUser(owning);
			Device saved = deviceRepo.save(model);
			return deviceMpr.asResponse(saved);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SAVE);
		}
	}

	@Override
	public DeviceRes findById(Long id) {
		try {
			Optional<Device> queried = deviceRepo.findById(id);
			if (!queried.isPresent())
				throw new ServiceExc(Failed.FIND_BY_ID_NO_CONTENT);
			return deviceMpr.asResponse(queried.get());
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_BY_ID);
		}
	}

	@Override
	public Multi<DeviceRes> findAll(int page, int size) {
		try {
			var pageable = deviceRepo.findAll(PageRequest.of(page, size));
			List<DeviceRes> values = deviceMpr.asListResponse(pageable.getContent());
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
	public Multi<DeviceRes> findAllArchived(int page, int size) {
		try {
			var pageable = deviceRepo.findAllByDeletedTrue(PageRequest.of(page, size));
			List<DeviceRes> values = deviceMpr.asListResponse(pageable.getContent());
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
	public DeviceRes updateById(Long id, DeviceUpd update) {
		try {
			var value = ensureExistedById(id);
			Device model = deviceRepo.save(value);
			return deviceMpr.asResponse(model);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.UPDATE);
		}
	}

	@Override
	public DeviceRes deleteById(Long id) {
		try {
			var value = ensureExistedById(id);
			DeviceRes response = deviceMpr.asResponse(value);
			deviceRepo.delete(value);
			return response;
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.DELETE);
		}
	}
}
