package spring.iam.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.iam.model.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {
  @Query(value = "SELECT * FROM Device WHERE deleted = TRUE", nativeQuery = true)
  Page<Device> findAllByDeletedTrue(Pageable pageable);

  Optional<Device> findByIpAddress(String ipAddress);
}
