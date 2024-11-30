package spring.iam.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.iam.model.Status;

@Repository
public interface StatusRepo extends JpaRepository<Status, Long> {
  @Query(value = "SELECT * FROM Status WHERE deleted = TRUE", nativeQuery = true)
  Page<Status> findAllByDeletedTrue(Pageable pageable);

  Optional<Status> findByUserId(Long userId);
}
