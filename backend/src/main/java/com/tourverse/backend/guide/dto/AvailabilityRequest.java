package com.tourverse.backend.guide.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityRequest {

	// The specific date the guide is setting their schedule for.
	private LocalDate date;

	// A simple flag to mark the entire day as available or on holiday.
	@Builder.Default
	private boolean isAvailable = true;

	// A list of start times for available slots (e.g., 09:00, 10:00, 14:00).
	// This will be empty if 'isAvailable' is false.
	private List<LocalTime> slots;
}