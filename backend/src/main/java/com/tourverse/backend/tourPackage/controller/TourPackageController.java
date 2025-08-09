package com.tourverse.backend.tourPackage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tourverse.backend.tourPackage.dto.CreateTourPackageRequest;
import com.tourverse.backend.tourPackage.dto.TourPackageDto;
import com.tourverse.backend.tourPackage.services.TourPackageService;

import java.util.List;

@RestController
@RequestMapping("/api") // Base mapping for the controller
@RequiredArgsConstructor
public class TourPackageController {

	private final TourPackageService tourPackageService;

	// =================================================================
	// PUBLIC ENDPOINTS - Accessible to everyone
	// =================================================================

	@GetMapping("/public/tours")
	public ResponseEntity<List<TourPackageDto>> getAllTourPackages() {
		List<TourPackageDto> packages = tourPackageService.getAllPackages();
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/public/tours/{packageId}")
	public ResponseEntity<TourPackageDto> getTourPackageById(@PathVariable Long packageId) {
		TourPackageDto packageDto = tourPackageService.getPackageById(packageId);
		return ResponseEntity.ok(packageDto);
	}

	// =================================================================
	// ADMIN-ONLY ENDPOINTS - Secured by SecurityConfig
	// =================================================================

	@PostMapping("/admin/tours")
	public ResponseEntity<TourPackageDto> createTourPackage(@ModelAttribute CreateTourPackageRequest request) {
		TourPackageDto createdPackage = tourPackageService.createPackage(request);
		return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
	}

	@PutMapping("/admin/tours/{packageId}")
	public ResponseEntity<TourPackageDto> updateTourPackage(@PathVariable Long packageId,
			@ModelAttribute CreateTourPackageRequest request) {
		TourPackageDto updatedPackage = tourPackageService.updatePackage(packageId, request);
		return ResponseEntity.ok(updatedPackage);
	}

	@DeleteMapping("/admin/tours/{packageId}")
	public ResponseEntity<Void> deleteTourPackage(@PathVariable Long packageId) {
		tourPackageService.deletePackage(packageId);
		return ResponseEntity.noContent().build();
	}
}