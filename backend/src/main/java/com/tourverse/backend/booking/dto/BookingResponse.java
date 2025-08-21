package com.tourverse.backend.booking.dto;

import com.tourverse.backend.booking.entity.Booking;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponse {
	private Long id;
	private Long guideId;
	private String guideName;
	private Long travelerId;
	private String travelerName;
	private LocalDate tourDate;
	private LocalTime tourTime;
	private int hours;
	private BigDecimal totalAmount;
	private Booking.BookingStatus status;
	private String location;
}