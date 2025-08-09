package com.tourverse.backend.guide.controller;

import com.tourverse.backend.guide.dto.GuideSearchResultDto;
import com.tourverse.backend.guide.service.GuideSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public/guides") // Placed under the public API
@RequiredArgsConstructor
public class GuideSearchController {

	private final GuideSearchService guideSearchService;

	/**
	 * Public endpoint to search for available guides.
	 *
	 * @param date     The desired date for the tour (in YYYY-MM-DD format).
	 * @param location The desired location for the tour.
	 * @param language The desired language for the tour.
	 * @return A list of guides who match the criteria and are available.
	 */
	@GetMapping("/search")
	public ResponseEntity<List<GuideSearchResultDto>> searchGuides(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String location,
			@RequestParam String language) {

		List<GuideSearchResultDto> availableGuides = guideSearchService.searchAvailableGuides(date, location, language);
		return ResponseEntity.ok(availableGuides);
	}
}