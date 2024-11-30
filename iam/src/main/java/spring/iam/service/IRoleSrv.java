package spring.iam.service;

import java.util.Set;
import spring.iam.model.Privilege;
import spring.iam.model.Role;
import spring.iam.model.dto.RoleCre;
import spring.iam.model.dto.RoleRes;
import spring.iam.model.dto.RoleUpd;
import spring.iam.response.Multi;

public interface IRoleSrv {
  void ensureNotExistedByName(String name);

  Role ensureExistedById(Long id);

  Set<Privilege> ensureOwningExistedByIds(Set<Long> ids);

  RoleRes save(RoleCre creation);

  RoleRes findById(Long id);

  Multi<RoleRes> findAll(int page, int size);

  Multi<RoleRes> findAllArchived(int page, int size);

  RoleRes updateById(Long id, RoleUpd update);

  RoleRes deleteById(Long id);
}
