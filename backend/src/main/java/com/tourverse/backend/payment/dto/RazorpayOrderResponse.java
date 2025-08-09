package com.tourverse.backend.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RazorpayOrderResponse {
	private String razorpayOrderId;
	private Long bookingId;
	private String razorpayKeyId;
	private double amount;
}