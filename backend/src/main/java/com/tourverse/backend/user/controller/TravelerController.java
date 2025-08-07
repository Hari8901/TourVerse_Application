package com.tourverse.backend.user.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.user.dto.TravelerDto;
import com.tourverse.backend.user.dto.TravelerUpdateRequest;

import com.tourverse.backend.user.service.TravelerService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/traveler")
@RequiredArgsConstructor
public class TravelerController {

	private final TravelerService travelerService;

	@GetMapping("/profile")
	public ResponseEntity<TravelerDto> getProfile(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		TravelerDto dto = travelerService.getProfile(principal.getUser().getId());
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/profile/update")
	public ResponseEntity<TravelerDto> updateProfile(@ModelAttribute TravelerUpdateRequest req, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		TravelerDto dto = travelerService.updateProfile(principal.getUser().getId(), req);
		return ResponseEntity.ok(dto);
	}
}