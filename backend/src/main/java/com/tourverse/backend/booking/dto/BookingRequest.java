package com.tourverse.backend.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
	@NotNull(message = "Tour date is required")
	@Future(message = "Tour date must be in the future")
	private LocalDate tourDate;

	@NotNull(message = "Tour time is required")
	private LocalTime tourTime;

	@Min(value = 1, message = "Booking must be for at least 1 hour")
	private int hours;

	@NotBlank(message = "Location is required")
	private String location;
}