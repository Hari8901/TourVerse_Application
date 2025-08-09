package com.tourverse.backend.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tourverse.backend.admin.dto.GuideVerificationDto;
import com.tourverse.backend.admin.dto.UserViewDto;
import com.tourverse.backend.admin.service.AdminService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	// --- Guide Verification Endpoints ---

	@GetMapping("/guides/unverified")
	public ResponseEntity<List<GuideVerificationDto>> getUnverifiedGuides() {
		return ResponseEntity.ok(adminService.getUnverifiedGuides());
	}

	@PostMapping("/guides/verify/{guideId}")
	public ResponseEntity<String> verifyGuide(@PathVariable Long guideId, @RequestBody Map<String, Boolean> approval) {
		boolean isApproved = approval.getOrDefault("approve", false);
		adminService.verifyGuide(guideId, isApproved);
		String message = isApproved ? "Guide approved successfully." : "Guide rejected successfully.";
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

	@PostMapping("/users/block/{userId}")
	public ResponseEntity<String> blockUser(@PathVariable Long userId) {
		adminService.blockUser(userId);
		return ResponseEntity.ok("User blocked successfully.");
	}

	@PostMapping("/users/unblock/{userId}")
	public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
		adminService.unblockUser(userId);
		return ResponseEntity.ok("User unblocked successfully.");
	}
}