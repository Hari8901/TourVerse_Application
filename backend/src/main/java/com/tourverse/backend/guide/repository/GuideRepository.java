package com.tourverse.backend.guide.repository;

import com.tourverse.backend.guide.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
	Optional<Guide> findByEmail(String email);

	List<Guide> findByVerificationStatus(Guide.VerificationStatus status);

	// Updated to fetch only approved guides
	List<Guide> findAllByIdInAndLocationAndLanguagesContainingAndVerificationStatus(List<Long> ids, String location,
			String language, Guide.VerificationStatus verificationStatus);

	// For the booking service to find approved guides in a location
	List<Guide> findByLocationAndVerificationStatus(String location, Guide.VerificationStatus verificationStatus);
}