package spring.iam.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spring.iam.model.Privilege;

@Repository
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
	@Query(value = "SELECT COUNT(P.id) FROM Privilege AS P WHERE P.name = :name AND deleted IN (TRUE, FALSE)", nativeQuery = true)
	long countExistedByName(@Param("name") String name);

	@Query(value = "SELECT * FROM Privilege WHERE deleted = TRUE", nativeQuery = true)
	Page<Privilege> findAllByDeletedTrue(Pageable pageable);

	Optional<Privilege> findByName(String name);
}
