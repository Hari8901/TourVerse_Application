package com.tourverse.backend.auth.controller;

import com.tourverse.backend.auth.dto.OtpRequest;
import com.tourverse.backend.auth.dto.ResetPasswordRequest;
import com.tourverse.backend.user.dto.TravelerLoginRequest;
import com.tourverse.backend.user.dto.TravelerRegisterRequest;
import com.tourverse.backend.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/traveler")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/init")
    public ResponseEntity<String> initiateRegistration(@ModelAttribute TravelerRegisterRequest req) {
        authService.initiateRegistration(req);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/register/verify")
    public ResponseEntity<String> verifyAndCompleteRegistration(@ModelAttribute TravelerRegisterRequest req) {
        authService.verifyAndCompleteRegistration(req);
        return ResponseEntity.ok("Registration successful. Please login.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TravelerLoginRequest req) {
        String jwt = authService.login(req);
        if (jwt == null) {
            return ResponseEntity.accepted().body("OTP sent; re-submit request with otp field.");
        }
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok("Successfully logged out.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendPasswordResetOtp(@RequestBody OtpRequest req) {
        authService.sendPasswordResetOtp(req.getEmail());
        return ResponseEntity.ok("Password reset OTP sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req.getEmail(), req.getNewPassword(), req.getOldPassword(), req.getOtp());
        return ResponseEntity.ok("Password reset successful.");
    }
}
