package com.tourverse.backend.booking.controller;

import com.razorpay.RazorpayException;
import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.booking.dto.PackageBookingRequest;
import com.tourverse.backend.booking.entity.PackageBooking;
import com.tourverse.backend.booking.service.PackageBookingService;
import com.tourverse.backend.common.exceptions.PaymentException;
import com.tourverse.backend.common.exceptions.ResourceNotFoundException;
import com.tourverse.backend.payment.dto.RazorpayOrderResponse;
import com.tourverse.backend.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings/package")
@RequiredArgsConstructor
public class PackageBookingController {

	private final PackageBookingService packageBookingService;
	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<RazorpayOrderResponse> purchasePackage(Authentication auth,
			@RequestBody PackageBookingRequest request) throws ResourceNotFoundException {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		try {
			PackageBooking createdBooking = packageBookingService.createPackageBooking(principal.getUser().getId(),
					request);

			RazorpayOrderResponse response = paymentService.createOrderForPackageBooking(createdBooking.getId(),
					principal.getUser().getId());
			return ResponseEntity.ok(response);
		} catch (RazorpayException e) {
			throw new PaymentException("Failed to create Razorpay order for package booking.", e);
		}
	}
}