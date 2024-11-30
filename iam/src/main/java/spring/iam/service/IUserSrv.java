package spring.iam.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import spring.iam.model.Role;
import spring.iam.model.User;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;
import spring.iam.model.dto.UserUpd;
import spring.iam.response.Multi;

public interface IUserSrv {
	UserDetailsService userDetailsService();

	void ensureNotExistedByUsername(String username);

	User ensureExistedById(Long id);

	Set<Role> ensureOwningExistedByIds(Set<Long> ids);

	UserRes save(UserCre creation);

	UserRes findById(Long id);

	UserRes findByUsername(String username);

	Multi<UserRes> findAll(int page, int size);

	Multi<UserRes> findAllArchived(int page, int size);

	UserRes updateById(Long id, UserUpd update);

	UserRes deleteById(Long id);
}
