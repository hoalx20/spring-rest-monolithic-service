package spring.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.iam.model.BadCredential;

public interface BadCredentialRepo extends JpaRepository<BadCredential, Long> {

	boolean existsByAccessTokenId(String id);
}
