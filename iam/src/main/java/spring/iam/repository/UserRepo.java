package spring.iam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spring.iam.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	@Query(value = "SELECT COUNT(U.id) FROM User AS U WHERE U.username = :username AND deleted IN (TRUE, FALSE)", nativeQuery = true)
	long countExistedByUsername(@Param("username") String username);

	@Query(value = "SELECT * FROM User WHERE deleted = TRUE", nativeQuery = true)
	Page<User> findAllByDeletedTrue(Pageable pageable);
}