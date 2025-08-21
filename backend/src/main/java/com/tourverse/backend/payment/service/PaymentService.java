package com.tourverse.backend.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.entity.PackageBooking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.booking.repository.PackageBookingRepository;
import com.tourverse.backend.common.exceptions.ResourceNotFoundException;
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

	@Transactional
	public RazorpayOrderResponse createOrderForGuideBooking(Long bookingId, Long travelerId)
			throws RazorpayException, ResourceNotFoundException {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

		if (!booking.getTraveler().getId().equals(travelerId)) {
			throw new IllegalStateException("You are not authorized to pay for this booking.");
		}
		if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
			throw new IllegalStateException("Payment can only be made for completed bookings.");
		}
		if (booking.getPaymentStatus() == Booking.PaymentStatus.PAID) {
			throw new IllegalStateException("This booking has already been paid for.");
		}

		Order order = createRazorpayOrder(booking.getTotalAmount(), "booking_" + booking.getId().toString());
		booking.setRazorpayOrderId(order.get("id"));
		booking.setPaymentStatus(Booking.PaymentStatus.PAID); // Assuming payment is successful
		bookingRepository.save(booking);

		return buildResponse(order, booking.getId(), booking.getTotalAmount());
	}

	@Transactional
	public RazorpayOrderResponse createOrderForPackageBooking(Long packageBookingId, Long travelerId)
			throws RazorpayException, ResourceNotFoundException {
		PackageBooking booking = packageBookingRepository.findById(packageBookingId).orElseThrow(
				() -> new ResourceNotFoundException("Package booking not found with ID: " + packageBookingId));

		if (!booking.getTraveler().getId().equals(travelerId)) {
			throw new IllegalStateException("You are not authorized to pay for this booking.");
		}

		Order order = createRazorpayOrder(booking.getTotalAmount(), "package_" + booking.getId().toString());
		booking.setRazorpayOrderId(order.get("id"));
		booking.setStatus(PackageBooking.PackageBookingStatus.CONFIRMED);
		packageBookingRepository.save(booking);

		return buildResponse(order, booking.getId(), booking.getTotalAmount());
	}

	private Order createRazorpayOrder(BigDecimal amount, String receiptId) throws RazorpayException {
		RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amount.multiply(new BigDecimal("100")).longValue());
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", receiptId);

		return razorpayClient.orders.create(orderRequest);
	}

	private RazorpayOrderResponse buildResponse(Order order, Long bookingId, BigDecimal amount) {
		return RazorpayOrderResponse.builder().razorpayOrderId(order.get("id")).bookingId(bookingId)
				.razorpayKeyId(keyId).amount(amount.doubleValue()).build();
	}
}