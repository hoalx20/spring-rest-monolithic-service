package spring.iam.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spring.iam.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	@Query(value = "SELECT COUNT(R.id) FROM Role AS R WHERE R.name = :name AND deleted IN (TRUE, FALSE)", nativeQuery = true)
	long countExistedByName(@Param("name") String name);

	@Query(value = "SELECT * FROM Role WHERE deleted = TRUE", nativeQuery = true)
	Page<Role> findAllByDeletedTrue(Pageable pageable);

	Optional<Role> findByName(String name);
}