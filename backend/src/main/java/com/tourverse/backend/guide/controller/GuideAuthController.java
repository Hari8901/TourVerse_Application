package com.tourverse.backend.guide.controller;

import com.tourverse.backend.auth.dto.ChangePasswordRequest;
import com.tourverse.backend.auth.dto.OtpRequest;
import com.tourverse.backend.auth.dto.OtpVerificationRequest;
import com.tourverse.backend.auth.dto.ResetPasswordRequest;
import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.guide.dto.GuideDto;
import com.tourverse.backend.guide.dto.GuideLoginRequest;
import com.tourverse.backend.guide.dto.GuideRegisterRequest;
import com.tourverse.backend.guide.dto.GuideUpdateRequest;
import com.tourverse.backend.guide.service.GuideAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guide")
@RequiredArgsConstructor
public class GuideAuthController {

	private final GuideAuthService guideAuthService;

	// --- AUTHENTICATION ENDPOINTS ---

	@PostMapping("/register/init")
	public ResponseEntity<String> initiateRegistration(@ModelAttribute GuideRegisterRequest req) {
		guideAuthService.initiateRegistration(req);
		return ResponseEntity.ok("OTP sent to your email for guide registration.");
	}

	@PostMapping("/register/verify")
	public ResponseEntity<String> verifyAndCompleteRegistration(@ModelAttribute GuideRegisterRequest req) {
		guideAuthService.verifyAndCompleteRegistration(req);
		return ResponseEntity.ok("Guide registration successful. Please login.");
	}

	@PostMapping("/login/init")
	public ResponseEntity<String> loginInit(@RequestBody GuideLoginRequest req) {
		guideAuthService.loginInit(req);
		return ResponseEntity.accepted().body("OTP sent; re-submit request with OTP to complete login.");
	}

	@PostMapping("/login/verify")
	public ResponseEntity<String> loginVerify(@RequestBody OtpVerificationRequest req) {
		String jwt = guideAuthService.loginVerify(req);
		return ResponseEntity.ok(jwt);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		guideAuthService.logout(authHeader);
		return ResponseEntity.ok("Successfully logged out.");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> sendPasswordResetOtp(@RequestBody OtpRequest req) {
		guideAuthService.sendPasswordResetOtp(req.getEmail());
		return ResponseEntity.ok("Password reset OTP sent.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
		guideAuthService.resetPassword(req.getEmail(), req.getNewPassword(), req.getOtp());
		return ResponseEntity.ok("Password reset successful.");
	}

	@PostMapping("/profile/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		guideAuthService.changePassword(principal.getUser().getId(), req);
		return ResponseEntity.ok("Password changed successfully.");
	}

	// --- PROFILE & DASHBOARD ENDPOINTS ---

	@GetMapping("/dashboard")
	public ResponseEntity<GuideDto> getDashboard(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		GuideDto dto = guideAuthService.getDashboard(principal.getUser().getId());
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/profile/update")
	public ResponseEntity<GuideDto> updateProfile(@ModelAttribute GuideUpdateRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		GuideDto updatedDto = guideAuthService.updateProfile(principal.getUser().getId(), req);
		return ResponseEntity.ok(updatedDto);
	}

	@DeleteMapping("/profile")
	public ResponseEntity<String> deleteProfile(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		guideAuthService.deleteGuide(principal.getUser().getId());
		return ResponseEntity.ok("Guide account deleted successfully.");
	}
}