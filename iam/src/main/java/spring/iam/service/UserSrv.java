package spring.iam.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Failed;
import spring.iam.exception.ServiceExc;
import spring.iam.mapper.UserMpr;
import spring.iam.model.Role;
import spring.iam.model.User;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;
import spring.iam.model.dto.UserUpd;
import spring.iam.repository.RoleRepo;
import spring.iam.repository.UserRepo;
import spring.iam.response.Multi;
import spring.iam.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSrv implements IUserSrv {
	UserRepo userRepo;
	RoleRepo roleRepo;
	UserMpr userMpr;

	@Override
	public void ensureNotExistedByUsername(String username) {
		if (userRepo.countExistedByUsername(username) > 0)
			throw new ServiceExc(Failed.ALREADY_EXISTED);
	}

	public User ensureExistedById(Long id) {
		Optional<User> old = userRepo.findById(id);
		if (!old.isPresent())
			throw new ServiceExc(Failed.NOT_EXISTS);
		return old.get();
	}

	public Set<Role> ensureOwningExistedByIds(Set<Long> ids) {
		var owning = roleRepo.findAllById(ids);
		if (CollectionUtils.isEmpty(owning))
			throw new ServiceExc(Failed.OWNING_SIDE_NOT_EXISTS);
		return new HashSet<>(owning);
	}

	@Override
	public UserRes save(UserCre creation) {
		try {
			ensureNotExistedByUsername(creation.getUsername());
			User model = userMpr.asModel(creation);
			var owning = ensureOwningExistedByIds(creation.getRoleIds());
			model.setRoles(owning);
			User saved = userRepo.save(model);
			return userMpr.asResponse(saved);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SAVE);
		}
	}

	@Override
	public UserRes findById(Long id) {
		try {
			Optional<User> queried = userRepo.findById(id);
			if (!queried.isPresent())
				throw new ServiceExc(Failed.FIND_BY_ID_NO_CONTENT);
			return userMpr.asResponse(queried.get());
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_BY_ID);
		}
	}

	@Override
	public Multi<UserRes> findAll(int page, int size) {
		try {
			var pageable = userRepo.findAll(PageRequest.of(page, size));
			List<UserRes> values = userMpr.asListResponse(pageable.getContent());
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
	public Multi<UserRes> findAllArchived(int page, int size) {
		try {
			var pageable = userRepo.findAllByDeletedTrue(PageRequest.of(page, size));
			List<UserRes> values = userMpr.asListResponse(pageable.getContent());
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
	public UserRes updateById(Long id, UserUpd update) {
		try {
			var value = ensureExistedById(id);
			userMpr.mergeModel(value, update);
			User model = userRepo.save(value);
			return userMpr.asResponse(model);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.UPDATE);
		}
	}

	@Override
	public UserRes deleteById(Long id) {
		try {
			var value = ensureExistedById(id);
			UserRes response = userMpr.asResponse(value);
			userRepo.delete(value);
			return response;
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.DELETE);
		}
	}
}
