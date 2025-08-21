package com.tourverse.backend.user.controller;

import com.tourverse.backend.auth.dto.ChangePasswordRequest;
import com.tourverse.backend.auth.dto.OtpRequest;
import com.tourverse.backend.auth.dto.OtpVerificationRequest;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/traveler")
@RequiredArgsConstructor
public class TravelerAuthController {

	private final TravelerAuthService travelerService;

	// --- AUTHENTICATION ENDPOINTS ---

	@PostMapping("/register/init")
	public ResponseEntity<String> initiateRegistration(@Valid @ModelAttribute TravelerRegisterRequest req) {
		travelerService.initiateRegistration(req);
		return ResponseEntity.ok("OTP sent to your email for traveler registration.");
	}

	@PostMapping("/register/verify")
	public ResponseEntity<String> verifyAndCompleteRegistration(@Valid @ModelAttribute TravelerRegisterRequest req) {
		travelerService.verifyAndCompleteRegistration(req);
		return ResponseEntity.ok("Traveler registration successful. Please login.");
	}

	@PostMapping("/login/init")
	public ResponseEntity<String> loginInit(@Valid @RequestBody TravelerLoginRequest req) {
		travelerService.loginInit(req);
		return ResponseEntity.accepted().body("OTP sent to your email. Please verify to complete login.");
	}

	@PostMapping("/login/verify")
	public ResponseEntity<String> loginVerify(@Valid @RequestBody OtpVerificationRequest req) {
		String jwt = travelerService.loginVerify(req);
		return ResponseEntity.ok(jwt);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		travelerService.logout(authHeader);
		return ResponseEntity.ok("Successfully logged out.");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> sendPasswordResetOtp(@Valid @RequestBody OtpRequest req) {
		travelerService.sendPasswordResetOtp(req.getEmail());
		return ResponseEntity.ok("Password reset OTP sent.");
	}

	@PostMapping("/reset-password/confirm")
	public ResponseEntity<String> resetPasswordWithOtp(@Valid @RequestBody ResetPasswordRequest req) {
		travelerService.resetPasswordWithOtp(req);
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
	public ResponseEntity<TravelerDto> updateProfile(@Valid @ModelAttribute TravelerUpdateRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		TravelerDto updatedDto = travelerService.updateProfile(principal.getUser().getId(), req);
		return ResponseEntity.ok(updatedDto);
	}

	@PostMapping("/profile/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		travelerService.changePassword(principal.getUser().getId(), req);
		return ResponseEntity.ok("Password changed successfully.");
	}

	@DeleteMapping("/profile")
	public ResponseEntity<String> deleteProfile(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		travelerService.deleteProfile(principal.getUser().getId());
		return ResponseEntity.ok("Traveler account deleted successfully.");
	}
}