package com.tourverse.backend.booking.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PackageBookingRequest {
	private Long packageId;
	private LocalDate travelDate;
	private int numberOfTravelers;
}