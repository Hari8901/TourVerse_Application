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

	List<Guide> findAllByIdInAndLocationAndLanguagesContaining(List<Long> ids, String location, String language);
}