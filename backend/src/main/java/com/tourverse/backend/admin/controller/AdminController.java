package com.tourverse.backend.admin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tourverse.backend.admin.dto.AdminRegisterRequest;
import com.tourverse.backend.admin.dto.ApprovalRequest;
import com.tourverse.backend.admin.dto.GuideVerificationDto;
import com.tourverse.backend.admin.dto.UserViewDto;
import com.tourverse.backend.admin.service.AdminService;
import com.tourverse.backend.auth.dto.OtpRequest;
import com.tourverse.backend.auth.dto.OtpVerificationRequest;
import com.tourverse.backend.auth.dto.ResetPasswordRequest;
import com.tourverse.backend.user.dto.TravelerLoginRequest;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	// --- REGISTRATION & LOGIN ---
	@PostMapping("/register/init")
	public ResponseEntity<String> initiateRegistration(@RequestBody AdminRegisterRequest request) {
		adminService.initiateRegistration(request);
		return new ResponseEntity<>("OTP sent to your email for admin registration.", HttpStatus.OK);
	}

	@PostMapping("/register/verify")
	public ResponseEntity<String> completeRegistration(@RequestBody AdminRegisterRequest request) {
		adminService.completeRegistration(request);
		return new ResponseEntity<>("Admin registration successful. Please login.", HttpStatus.OK);
	}

	@PostMapping("/login/init")
	public ResponseEntity<String> loginInit(@RequestBody TravelerLoginRequest req) {
		adminService.loginInit(req);
		return ResponseEntity.accepted().body("OTP sent. Please verify to complete login.");
	}

	@PostMapping("/login/verify")
	public ResponseEntity<String> loginVerify(@RequestBody OtpVerificationRequest req) {
		String jwt = adminService.loginVerify(req);
		return ResponseEntity.ok(jwt);
	}

	// --- PASSWORD MANAGEMENT ---
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody OtpRequest req) {
		adminService.sendPasswordResetOtp(req.getEmail());
		return ResponseEntity.ok("Password reset OTP sent to admin email.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
		adminService.resetPasswordWithOtp(req.getEmail(), req.getNewPassword(), req.getOtp());
		return ResponseEntity.ok("Admin password has been reset successfully.");
	}

	// --- Guide Verification Endpoints ---

	@GetMapping("/guides/unverified")
	public ResponseEntity<List<GuideVerificationDto>> getUnverifiedGuides() {
		return ResponseEntity.ok(adminService.getUnverifiedGuides());
	}

	@PostMapping("/guides/{guideId}/verification")
	public ResponseEntity<String> verifyGuide(@PathVariable Long guideId,
			@RequestBody ApprovalRequest approvalRequest) { // Use the new DTO
		adminService.verifyGuide(guideId, approvalRequest.isApprove());
		String message = approvalRequest.isApprove() ? "Guide approved successfully." : "Guide rejected successfully.";
		return ResponseEntity.ok(message);
	}

	// --- User Management Endpoints ---

	@GetMapping("/users/guides")
	public ResponseEntity<List<UserViewDto>> getAllGuides() {
		return ResponseEntity.ok(adminService.getAllGuides());
	}

	@GetMapping("/users/travelers")
	public ResponseEntity<List<UserViewDto>> getAllTravelers() {
		return ResponseEntity.ok(adminService.getAllTravelers());
	}

	@PostMapping("/users/{userId}/block")
	public ResponseEntity<String> blockUser(@PathVariable Long userId) {
		adminService.blockUser(userId);
		return ResponseEntity.ok("User blocked successfully.");
	}

	@PostMapping("/users/{userId}/unblock")
	public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
		adminService.unblockUser(userId);
		return ResponseEntity.ok("User unblocked successfully.");
	}
}