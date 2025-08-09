package com.tourverse.backend.guide.repository;

import com.tourverse.backend.guide.document.AvailabilitySlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends MongoRepository<AvailabilitySlot, String> {

	/**
	 * Finds the availability slot for a specific guide on a specific date. This is
	 * useful for checking or updating a single day's schedule.
	 */
	Optional<AvailabilitySlot> findByGuideIdAndDate(Long guideId, LocalDate date);

	/**
	 * Finds all availability slots for a guide within a given date range. This will
	 * power the feature to show a guide's schedule for a week or month.
	 */
	List<AvailabilitySlot> findByGuideIdAndDateBetween(Long guideId, LocalDate startDate, LocalDate endDate);

	/**
	 * Finds all available guides for a specific date. This is crucial for the
	 * traveler's search functionality. It filters out guides who are on holiday.
	 */
	List<AvailabilitySlot> findByDateAndIsAvailableTrue(LocalDate date);
}