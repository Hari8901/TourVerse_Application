package com.tourverse.backend.booking.controller;

import com.razorpay.RazorpayException;
import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.booking.dto.PackageBookingRequest;
import com.tourverse.backend.booking.entity.PackageBooking;
import com.tourverse.backend.booking.service.PackageBookingService;
import com.tourverse.backend.payment.dto.RazorpayOrderResponse; // Update this import
import com.tourverse.backend.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings/package")
@RequiredArgsConstructor
public class PackageBookingController {

    // You now need to inject PaymentService here as well to handle the order creation
    private final PackageBookingService packageBookingService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<RazorpayOrderResponse> purchasePackage(Authentication auth, @RequestBody PackageBookingRequest request) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        try {
            // This service method now needs to return the created PackageBooking object
            PackageBooking createdBooking = packageBookingService.createPackageBooking(principal.getUser().getId(), request);
            
            // Then create the Razorpay order
            RazorpayOrderResponse response = paymentService.createOrderForPackageBooking(createdBooking.getId(), principal.getUser().getId());
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}