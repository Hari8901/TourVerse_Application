package com.tourverse.backend.booking.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.booking.dto.BookingRequest;
import com.tourverse.backend.booking.dto.BookingResponse;
import com.tourverse.backend.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings") // A clear, central path for all booking-related actions
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	// =================================================================
	// TRAVELER ENDPOINTS
	// =================================================================

	/**
	 * Endpoint for a traveler to create a new booking request.
	 */
	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(Authentication auth, @RequestBody BookingRequest request) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		BookingResponse createdBooking = bookingService.createBooking(principal.getUser().getId(), request);
		return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
	}

	/**
	 * Endpoint for a traveler to view their own booking history.
	 */
	@GetMapping("/my-bookings/traveler")
	public ResponseEntity<List<BookingResponse>> getTravelerBookings(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<BookingResponse> bookings = bookingService.getBookingsForTraveler(principal.getUser().getId());
		return ResponseEntity.ok(bookings);
	}

	// =================================================================
	// GUIDE ENDPOINTS
	// =================================================================

	/**
	 * Endpoint for a guide to accept a pending booking.
	 */
	@PostMapping("/accept/{bookingId}")
	public ResponseEntity<BookingResponse> acceptBooking(Authentication auth, @PathVariable Long bookingId) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		BookingResponse confirmedBooking = bookingService.acceptBooking(principal.getUser().getId(), bookingId);
		return ResponseEntity.ok(confirmedBooking);
	}

	/**
	 * Endpoint for a guide to view all bookings assigned to them.
	 */
	@GetMapping("/my-bookings/guide")
	public ResponseEntity<List<BookingResponse>> getGuideBookings(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<BookingResponse> bookings = bookingService.getBookingsForGuide(principal.getUser().getId());
		return ResponseEntity.ok(bookings);
	}
}