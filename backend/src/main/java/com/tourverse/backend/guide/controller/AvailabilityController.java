package com.tourverse.backend.guide.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.guide.document.AvailabilitySlot;
import com.tourverse.backend.guide.dto.AvailabilityRequest;
import com.tourverse.backend.guide.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/guide/availability")
@RequiredArgsConstructor
public class AvailabilityController {

	private final AvailabilityService availabilityService;

	/**
	 * Endpoint for a guide to set or update their availability for a single day.
	 * This handles setting time slots or marking the day as a holiday.
	 */
	@PostMapping
	public ResponseEntity<AvailabilitySlot> setOrUpdateAvailability(Authentication auth,
			@RequestBody AvailabilityRequest request) {

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		AvailabilitySlot updatedSlot = availabilityService.setOrUpdateAvailability(principal.getUser().getId(),
				request);
		return ResponseEntity.ok(updatedSlot);
	}

	/**
	 * Endpoint for a guide to view their own schedule over a date range. This will
	 * power the calendar/dashboard view on the front-end.
	 */
	@GetMapping
	public ResponseEntity<List<AvailabilitySlot>> getAvailability(Authentication auth,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<AvailabilitySlot> schedule = availabilityService.getAvailabilityForDateRange(principal.getUser().getId(),
				startDate, endDate);
		return ResponseEntity.ok(schedule);
	}
}