package com.tourverse.backend.payment.controller;

import com.razorpay.RazorpayException;
import com.tourverse.backend.auth.util.UserPrincipal;
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

	@PostMapping("/create-order/guide-booking/{bookingId}")
	public ResponseEntity<RazorpayOrderResponse> createOrderForGuide(Authentication auth,
			@PathVariable Long bookingId) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		try {
			RazorpayOrderResponse response = paymentService.createOrderForGuideBooking(bookingId,
					principal.getUser().getId());
			return ResponseEntity.ok(response);
		} catch (RazorpayException e) {
			return ResponseEntity.status(500).body(null);
		}
	}
}