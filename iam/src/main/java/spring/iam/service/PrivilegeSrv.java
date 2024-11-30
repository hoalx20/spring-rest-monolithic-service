package spring.iam.service;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spring.iam.constant.Failed;
import spring.iam.exception.ServiceExc;
import spring.iam.mapper.PrivilegeMpr;
import spring.iam.model.Privilege;
import spring.iam.model.dto.PrivilegeCre;
import spring.iam.model.dto.PrivilegeRes;
import spring.iam.model.dto.PrivilegeUpd;
import spring.iam.repository.PrivilegeRepo;
import spring.iam.response.Multi;
import spring.iam.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivilegeSrv implements IPrivilegeSrv {
  PrivilegeRepo privilegeRepo;
  PrivilegeMpr privilegeMpr;

  @Override
  public void ensureNotExistedByName(String name) {
    if (privilegeRepo.countExistedByName(name) > 0) throw new ServiceExc(Failed.ALREADY_EXISTED);
  }

  @Override
  public Privilege ensureExistedById(Long id) {
    var old = privilegeRepo.findById(id);
    if (!old.isPresent()) throw new ServiceExc(Failed.NOT_EXISTS);
    return old.get();
  }

  @Override
  public PrivilegeRes save(PrivilegeCre creation) {
    try {
      ensureNotExistedByName(creation.getName());
      Privilege model = privilegeMpr.asModel(creation);
      Privilege saved = privilegeRepo.save(model);
      return privilegeMpr.asResponse(saved);
    } catch (ServiceExc e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceExc(Failed.SAVE);
    }
  }

  @Override
  public PrivilegeRes findById(Long id) {
    try {
      Optional<Privilege> queried = privilegeRepo.findById(id);
      if (!queried.isPresent()) throw new ServiceExc(Failed.FIND_BY_ID_NO_CONTENT);
      return privilegeMpr.asResponse(queried.get());
    } catch (ServiceExc e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceExc(Failed.FIND_BY_ID);
    }
  }

  @Override
  public Multi<PrivilegeRes> findAll(int page, int size) {
    try {
      var pageable = privilegeRepo.findAll(PageRequest.of(page, size));
      List<PrivilegeRes> values = privilegeMpr.asListResponse(pageable.getContent());
      if (CollectionUtils.isEmpty(values)) throw new ServiceExc(Failed.FIND_ALL_NO_CONTENT);
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
  public Multi<PrivilegeRes> findAllArchived(int page, int size) {
    try {
      var pageable = privilegeRepo.findAllByDeletedTrue(PageRequest.of(page, size));
      List<PrivilegeRes> values = privilegeMpr.asListResponse(pageable.getContent());
      if (CollectionUtils.isEmpty(values)) throw new ServiceExc(Failed.FIND_ALL_NO_CONTENT);
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
  public PrivilegeRes updateById(Long id, PrivilegeUpd update) {
    try {
      var value = ensureExistedById(id);
      privilegeMpr.mergeModel(value, update);
      Privilege model = privilegeRepo.save(value);
      return privilegeMpr.asResponse(model);
    } catch (ServiceExc e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceExc(Failed.UPDATE);
    }
  }

  @Override
  public PrivilegeRes deleteById(Long id) {
    try {
      var value = ensureExistedById(id);
      PrivilegeRes response = privilegeMpr.asResponse(value);
      privilegeRepo.deleteById(id);
      return response;
    } catch (ServiceExc e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceExc(Failed.DELETE);
    }
  }
}
