package com.tourverse.backend.booking.service;

import com.tourverse.backend.booking.dto.PackageBookingRequest;
import com.tourverse.backend.booking.entity.PackageBooking;
import com.tourverse.backend.booking.repository.PackageBookingRepository;
import com.tourverse.backend.tourPackage.entity.TourPackage;
import com.tourverse.backend.tourPackage.repository.TourPackageRepository;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PackageBookingService {

	private final PackageBookingRepository packageBookingRepository;
	private final TravelerRepository travelerRepository;
	private final TourPackageRepository tourPackageRepository;
	// The dependency on PaymentService is no longer needed here.

	/**
	 * Creates a booking for a pre-made package. This method is now only responsible
	 * for creating the booking record.
	 *
	 * @param travelerId The ID of the logged-in traveler.
	 * @param request    The request DTO containing the package ID and travel
	 *                   details.
	 * @return The newly created PackageBooking entity.
	 */
	@Transactional
	public PackageBooking createPackageBooking(Long travelerId, PackageBookingRequest request) {
		Traveler traveler = travelerRepository.findById(travelerId)
				.orElseThrow(() -> new RuntimeException("Traveler not found"));

		TourPackage tourPackage = tourPackageRepository.findById(request.getPackageId())
				.orElseThrow(() -> new RuntimeException("Tour package not found"));

		// Calculate the total cost
		BigDecimal totalAmount = tourPackage.getPrice().multiply(new BigDecimal(request.getNumberOfTravelers()));

		// Create the package booking with PENDING_PAYMENT status
		PackageBooking packageBooking = PackageBooking.builder().traveler(traveler).tourPackage(tourPackage)
				.travelDate(request.getTravelDate()).numberOfTravelers(request.getNumberOfTravelers())
				.totalAmount(totalAmount).status(PackageBooking.PackageBookingStatus.PENDING_PAYMENT).build();

		// Save and return the new booking entity
		return packageBookingRepository.save(packageBooking);
	}
}