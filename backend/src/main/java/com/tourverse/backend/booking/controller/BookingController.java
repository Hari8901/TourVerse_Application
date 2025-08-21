package com.tourverse.backend.booking.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.booking.dto.BookingRequest;
import com.tourverse.backend.booking.dto.BookingResponse;
import com.tourverse.backend.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping("/traveler/bookings/guide")
	public ResponseEntity<BookingResponse> createGuideBooking(Authentication auth,
			@Valid @RequestBody BookingRequest request) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		BookingResponse createdBooking = bookingService.createBooking(principal.getUser().getId(), request);
		return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
	}

	@GetMapping("/traveler/bookings")
	public ResponseEntity<List<BookingResponse>> getTravelerBookings(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<BookingResponse> bookings = bookingService.getBookingsForTraveler(principal.getUser().getId());
		return ResponseEntity.ok(bookings);
	}

	@PostMapping("/traveler/bookings/{bookingId}/complete")
	public ResponseEntity<BookingResponse> completeBooking(Authentication auth, @PathVariable Long bookingId) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		BookingResponse completedBooking = bookingService.completeBooking(principal.getUser().getId(), bookingId);
		return ResponseEntity.ok(completedBooking);
	}

	@PostMapping("/guide/bookings/{bookingId}/accept")
	public ResponseEntity<BookingResponse> acceptBooking(Authentication auth, @PathVariable Long bookingId) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		BookingResponse confirmedBooking = bookingService.acceptBooking(principal.getUser().getId(), bookingId);
		return ResponseEntity.ok(confirmedBooking);
	}

	@GetMapping("/guide/bookings/available")
	public ResponseEntity<List<BookingResponse>> getAvailableGuideBookings(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<BookingResponse> bookings = bookingService.getAvailableBookingsForGuide(principal.getUser().getId());
		return ResponseEntity.ok(bookings);
	}
}