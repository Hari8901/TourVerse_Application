package com.tourverse.backend.guide.service;

import com.tourverse.backend.guide.document.AvailabilitySlot;
import com.tourverse.backend.guide.dto.AvailabilityRequest; // To be created
import com.tourverse.backend.guide.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

	private final AvailabilityRepository availabilityRepository;

	/**
	 * Sets or updates a guide's availability for a specific day. This can be used
	 * to set available time slots or mark a day as a holiday.
	 *
	 * @param guideId The ID of the guide whose availability is being set.
	 * @param request The DTO containing the date, availability status, and time
	 *                slots.
	 */
	@Transactional
	public AvailabilitySlot setOrUpdateAvailability(Long guideId, AvailabilityRequest request) {
		// Find an existing slot or create a new one
		AvailabilitySlot slot = availabilityRepository.findByGuideIdAndDate(guideId, request.getDate())
				.orElse(AvailabilitySlot.builder().guideId(guideId).date(request.getDate()).build());

		slot.setAvailable(request.isAvailable());
		slot.setSlots(request.getSlots()); // Set the list of available times

		return availabilityRepository.save(slot);
	}

	/**
	 * Retrieves a guide's availability schedule for a given date range.
	 *
	 * @param guideId   The ID of the guide.
	 * @param startDate The start of the date range.
	 * @param endDate   The end of the date range.
	 * @return A list of availability slots.
	 */
	@Transactional(readOnly = true)
	public List<AvailabilitySlot> getAvailabilityForDateRange(Long guideId, LocalDate startDate, LocalDate endDate) {
		return availabilityRepository.findByGuideIdAndDateBetween(guideId, startDate, endDate);
	}

	/**
	 * Finds all guides who are marked as available on a given date. This is the
	 * first step for a traveler's search.
	 *
	 * @param date The date to search for.
	 * @return A list of availability slots for guides who are available.
	 */
	@Transactional(readOnly = true)
	public List<AvailabilitySlot> getAvailableGuides(LocalDate date) {
		return availabilityRepository.findByDateAndIsAvailableTrue(date);
	}
}
