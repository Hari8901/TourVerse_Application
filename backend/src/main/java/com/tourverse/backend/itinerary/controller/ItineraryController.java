package com.tourverse.backend.itinerary.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.itinerary.dto.ItineraryRequest;
import com.tourverse.backend.itinerary.dto.ItineraryResponse;
import com.tourverse.backend.itinerary.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traveler/itineraries") // Secured under the traveler-specific path
@RequiredArgsConstructor
public class ItineraryController {

	private final ItineraryService itineraryService;

	/**
	 * Endpoint for a traveler to create a new custom itinerary.
	 */
	@PostMapping
	public ResponseEntity<ItineraryResponse> createItinerary(Authentication auth,
			@RequestBody ItineraryRequest request) {

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		ItineraryResponse createdItinerary = itineraryService.createItinerary(principal.getUser().getId(), request);
		return new ResponseEntity<>(createdItinerary, HttpStatus.CREATED);
	}

	/**
	 * Endpoint for a traveler to retrieve all of their saved itineraries.
	 */
	@GetMapping
	public ResponseEntity<List<ItineraryResponse>> getMyItineraries(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<ItineraryResponse> itineraries = itineraryService.getMyItineraries(principal.getUser().getId());
		return ResponseEntity.ok(itineraries);
	}

	/**
	 * Endpoint for a traveler to update one of their itineraries.
	 */
	@PutMapping("/{itineraryId}")
	public ResponseEntity<ItineraryResponse> updateItinerary(Authentication auth, @PathVariable Long itineraryId,
			@RequestBody ItineraryRequest request) {

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		ItineraryResponse updatedItinerary = itineraryService.updateItinerary(principal.getUser().getId(), itineraryId,
				request);
		return ResponseEntity.ok(updatedItinerary);
	}

	/**
	 * Endpoint for a traveler to delete one of their itineraries.
	 */
	@DeleteMapping("/{itineraryId}")
	public ResponseEntity<Void> deleteItinerary(Authentication auth, @PathVariable Long itineraryId) {

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		itineraryService.deleteItinerary(principal.getUser().getId(), itineraryId);
		return ResponseEntity.noContent().build();
	}
}