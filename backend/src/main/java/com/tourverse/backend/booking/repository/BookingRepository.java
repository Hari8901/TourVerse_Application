package com.tourverse.backend.booking.repository;

import com.tourverse.backend.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	// Find bookings for a specific traveler
	List<Booking> findByTravelerId(Long travelerId);

	// Find bookings for a specific guide
	List<Booking> findByGuideId(Long guideId);

	// Find pending bookings in a specific location for guides to view
	List<Booking> findByLocationAndStatus(String location, Booking.BookingStatus status);
}