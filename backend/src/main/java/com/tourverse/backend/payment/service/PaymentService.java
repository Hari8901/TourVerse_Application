package com.tourverse.backend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.entity.PackageBooking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.booking.repository.PackageBookingRepository;
import com.tourverse.backend.payment.dto.RazorpayOrderResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

	@Value("${razorpay.api.key-id}")
	private String keyId;

	@Value("${razorpay.api.key-secret}")
	private String keySecret;

	private final BookingRepository bookingRepository;
	private final PackageBookingRepository packageBookingRepository;

	/**
	 * Creates a Razorpay Order for a guide booking.
	 */
	@Transactional
	public RazorpayOrderResponse createOrderForGuideBooking(Long bookingId, Long travelerId) throws RazorpayException {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getTraveler().getId().equals(travelerId)) {
			throw new IllegalStateException("You are not authorized to pay for this booking.");
		}
		if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
			throw new IllegalStateException("Payment can only be made for confirmed bookings.");
		}

		Order order = createRazorpayOrder(booking.getTotalAmount(), booking.getId().toString());
		booking.setRazorpayOrderId(order.get("id"));
		bookingRepository.save(booking);

		return buildResponse(order, booking.getId(), booking.getTotalAmount());
	}

	/**
	 * Creates a Razorpay Order for a pre-made package booking.
	 */
	@Transactional
	public RazorpayOrderResponse createOrderForPackageBooking(Long packageBookingId, Long travelerId)
			throws RazorpayException {
		PackageBooking booking = packageBookingRepository.findById(packageBookingId)
				.orElseThrow(() -> new RuntimeException("Package booking not found"));

		if (!booking.getTraveler().getId().equals(travelerId)) {
			throw new IllegalStateException("You are not authorized to pay for this booking.");
		}

		Order order = createRazorpayOrder(booking.getTotalAmount(), booking.getId().toString());
		booking.setRazorpayOrderId(order.get("id"));
		packageBookingRepository.save(booking);

		return buildResponse(order, booking.getId(), booking.getTotalAmount());
	}

	// --- Private Helper Methods ---

	private Order createRazorpayOrder(BigDecimal amount, String receiptId) throws RazorpayException {
		RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amount.multiply(new BigDecimal("100")).longValue()); // Amount in smallest currency
																						// unit (e.g., paise)
		orderRequest.put("currency", "INR"); // Change as needed
		orderRequest.put("receipt", receiptId);

		return razorpayClient.orders.create(orderRequest);
	}

	private RazorpayOrderResponse buildResponse(Order order, Long bookingId, BigDecimal amount) {
		return RazorpayOrderResponse.builder().razorpayOrderId(order.get("id")).bookingId(bookingId)
				.razorpayKeyId(keyId) // Pass the public key ID to the frontend
				.amount(amount.doubleValue()).build();
	}
}