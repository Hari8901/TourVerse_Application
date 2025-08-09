package com.tourverse.backend.guide.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "availability_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilitySlot {

	@Id
	private String id;

	@Field("guide_id")
	private Long guideId;

	private LocalDate date;

	// If 'isAvailable' is false, the guide is on holiday or unavailable for the
	// entire day.
	// The 'slots' list will be ignored in this case.
	@Field("is_available")
	@Builder.Default
	private boolean isAvailable = true;

	// A list of available time slots for the day (e.g., 09:00, 10:00, 11:00)
	private List<LocalTime> slots;
}