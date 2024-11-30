package spring.iam.service;

import spring.iam.model.Privilege;
import spring.iam.model.dto.PrivilegeCre;
import spring.iam.model.dto.PrivilegeRes;
import spring.iam.model.dto.PrivilegeUpd;
import spring.iam.response.Multi;

public interface IPrivilegeSrv {
  void ensureNotExistedByName(String name);

  Privilege ensureExistedById(Long id);

  PrivilegeRes save(PrivilegeCre creation);

  PrivilegeRes findById(Long id);

  Multi<PrivilegeRes> findAll(int page, int size);

  Multi<PrivilegeRes> findAllArchived(int page, int size);

  PrivilegeRes updateById(Long id, PrivilegeUpd update);

  PrivilegeRes deleteById(Long id);
}
