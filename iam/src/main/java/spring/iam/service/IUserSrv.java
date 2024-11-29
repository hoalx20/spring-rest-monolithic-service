package spring.iam.service;

import java.util.Set;

import spring.iam.model.Role;
import spring.iam.model.User;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;
import spring.iam.model.dto.UserUpd;
import spring.iam.response.Multi;

public interface IUserSrv {
	void ensureNotExistedByUsername(String username);

	User ensureExistedById(Long id);

	Set<Role> ensureOwningExistedByIds(Set<Long> ids);

	UserRes save(UserCre creation);

	UserRes findById(Long id);

	Multi<UserRes> findAll(int page, int size);

	Multi<UserRes> findAllArchived(int page, int size);

	UserRes updateById(Long id, UserUpd update);

	UserRes deleteById(Long id);
}
