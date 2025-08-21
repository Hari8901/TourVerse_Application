package com.tourverse.backend.payment.controller;

import com.razorpay.RazorpayException;
import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.common.exceptions.PaymentException;
import com.tourverse.backend.common.exceptions.ResourceNotFoundException;
import com.tourverse.backend.payment.dto.RazorpayOrderResponse;
import com.tourverse.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/order/guide-booking/{bookingId}")
	public ResponseEntity<RazorpayOrderResponse> createOrderForGuideBooking(Authentication auth,
			@PathVariable Long bookingId) throws ResourceNotFoundException {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		try {
			RazorpayOrderResponse response = paymentService.createOrderForGuideBooking(bookingId,
					principal.getUser().getId());
			return ResponseEntity.ok(response);
		} catch (RazorpayException e) {
			throw new PaymentException("Failed to create Razorpay order for guide booking.", e);
		}
	}

	@PostMapping("/order/package-booking/{bookingId}")
	public ResponseEntity<RazorpayOrderResponse> createOrderForPackageBooking(Authentication auth,
			@PathVariable Long bookingId) throws ResourceNotFoundException {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		try {
			RazorpayOrderResponse response = paymentService.createOrderForPackageBooking(bookingId,
					principal.getUser().getId());
			return ResponseEntity.ok(response);
		} catch (RazorpayException e) {
			throw new PaymentException("Failed to create Razorpay order for package booking.", e);
		}
	}
}