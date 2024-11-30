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
import spring.iam.mapper.RoleMpr;
import spring.iam.model.Privilege;
import spring.iam.model.Role;
import spring.iam.model.dto.RoleCre;
import spring.iam.model.dto.RoleRes;
import spring.iam.model.dto.RoleUpd;
import spring.iam.repository.PrivilegeRepo;
import spring.iam.repository.RoleRepo;
import spring.iam.response.Multi;
import spring.iam.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleSrv implements IRoleSrv {
	RoleRepo roleRepo;
	PrivilegeRepo privilegeRepo;
	RoleMpr roleMpr;

	@Override
	public void ensureNotExistedByName(String name) {
		if (roleRepo.countExistedByName(name) > 0)
			throw new ServiceExc(Failed.ALREADY_EXISTED);
	}

	@Override
	public Role ensureExistedById(Long id) {
		var queried = roleRepo.findById(id);
		if (!queried.isPresent())
			throw new ServiceExc(Failed.NOT_EXISTS);
		return queried.get();
	}

	@Override
	public Set<Privilege> ensureOwningExistedByIds(Set<Long> ids) {
		var owning = privilegeRepo.findAllById(ids);
		if (CollectionUtils.isEmpty(owning))
			throw new ServiceExc(Failed.OWNING_SIDE_NOT_EXISTS);
		return new HashSet<>(owning);
	}

	@Override
	public RoleRes save(RoleCre creation) {
		try {
			ensureNotExistedByName(creation.getName());
			Role model = roleMpr.asModel(creation);
			var owning = ensureOwningExistedByIds(creation.getPrivilegeIds());
			model.setPrivileges(owning);
			Role saved = roleRepo.save(model);
			return roleMpr.asResponse(saved);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SAVE);
		}
	}

	@Override
	public RoleRes findById(Long id) {
		try {
			Optional<Role> queried = roleRepo.findById(id);
			if (!queried.isPresent())
				throw new ServiceExc(Failed.FIND_BY_ID_NO_CONTENT);
			return roleMpr.asResponse(queried.get());
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.FIND_BY_ID);
		}
	}

	@Override
	public Multi<RoleRes> findAll(int page, int size) {
		try {
			var pageable = roleRepo.findAll(PageRequest.of(page, size));
			List<RoleRes> values = roleMpr.asListResponse(pageable.getContent());
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
	public Multi<RoleRes> findAllArchived(int page, int size) {
		try {
			var pageable = roleRepo.findAllByDeletedTrue(PageRequest.of(page, size));
			List<RoleRes> values = roleMpr.asListResponse(pageable.getContent());
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
	public RoleRes updateById(Long id, RoleUpd update) {
		try {
			var value = ensureExistedById(id);
			roleMpr.mergeModel(value, update);
			Role model = roleRepo.save(value);
			return roleMpr.asResponse(model);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.UPDATE);
		}
	}

	@Override
	public RoleRes deleteById(Long id) {
		try {
			var value = ensureExistedById(id);
			RoleRes response = roleMpr.asResponse(value);
			roleRepo.delete(value);
			return response;
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.DELETE);
		}
	}
}
