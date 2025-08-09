package com.tourverse.backend.booking.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
	private Long guideId;
	private LocalDate tourDate;
	private LocalTime tourTime;
	private int hours;
}