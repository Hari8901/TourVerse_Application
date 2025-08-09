package com.tourverse.backend.user.controller;

import com.tourverse.backend.auth.dto.OtpRequest;
import com.tourverse.backend.auth.dto.ResetPasswordRequest;
import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.user.dto.TravelerDto;
import com.tourverse.backend.user.dto.TravelerLoginRequest;
import com.tourverse.backend.user.dto.TravelerRegisterRequest;
import com.tourverse.backend.user.dto.TravelerUpdateRequest;
import com.tourverse.backend.user.service.TravelerAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/traveler")
@RequiredArgsConstructor
public class TravelerAuthController {

	private final TravelerAuthService travelerService;

	// --- AUTHENTICATION ENDPOINTS ---

	@PostMapping("/register/init")
	public ResponseEntity<String> initiateRegistration(@ModelAttribute TravelerRegisterRequest req) {
		travelerService.initiateRegistration(req);
		return ResponseEntity.ok("OTP sent to your email for traveler registration.");
	}

	@PostMapping("/register/verify")
	public ResponseEntity<String> verifyAndCompleteRegistration(@ModelAttribute TravelerRegisterRequest req) {
		travelerService.verifyAndCompleteRegistration(req);
		return ResponseEntity.ok("Traveler registration successful. Please login.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody TravelerLoginRequest req) {
		String jwt = travelerService.login(req);
		if (jwt == null) {
			return ResponseEntity.accepted().body("OTP sent; re-submit request with OTP to complete login.");
		}
		return ResponseEntity.ok(jwt);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		travelerService.logout(authHeader);
		return ResponseEntity.ok("Successfully logged out.");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> sendPasswordResetOtp(@RequestBody OtpRequest req) {
		travelerService.sendPasswordResetOtp(req.getEmail());
		return ResponseEntity.ok("Password reset OTP sent.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
		travelerService.resetPassword(req.getEmail(), req.getNewPassword(), req.getOldPassword(), req.getOtp());
		return ResponseEntity.ok("Password reset successful.");
	}

	// --- PROFILE & DASHBOARD ENDPOINTS ---

	@GetMapping("/dashboard")
	public ResponseEntity<TravelerDto> getDashboard(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		TravelerDto dto = travelerService.getDashboard(principal.getUser().getId());
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/profile/update")
	public ResponseEntity<TravelerDto> updateProfile(@ModelAttribute TravelerUpdateRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		TravelerDto updatedDto = travelerService.updateProfile(principal.getUser().getId(), req);
		return ResponseEntity.ok(updatedDto);
	}

	@DeleteMapping("/profile")
	public ResponseEntity<String> deleteProfile(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		travelerService.deleteProfile(principal.getUser().getId());
		return ResponseEntity.ok("Traveler account deleted successfully.");
	}
}