package com.tourverse.backend.guide.service;

import com.tourverse.backend.guide.document.AvailabilitySlot;
import com.tourverse.backend.guide.dto.GuideSearchResultDto;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.repository.AvailabilityRepository;
import com.tourverse.backend.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideSearchService {

    private final GuideRepository guideRepository;
    private final AvailabilityRepository availabilityRepository;

    /**
     * Searches for available guides based on date, location, and language.
     * This is a public-facing method.
     */
    @Transactional(readOnly = true)
    public List<GuideSearchResultDto> searchAvailableGuides(LocalDate date, String location, String language) {
        // Step 1: Find all guides who are available on the given date from MongoDB.
        List<AvailabilitySlot> availableSlots = availabilityRepository.findByDateAndIsAvailableTrue(date);
        List<Long> availableGuideIds = availableSlots.stream()
                .map(AvailabilitySlot::getGuideId)
                .collect(Collectors.toList());
        
        // If no guides are available on this date, return an empty list immediately.
        if (availableGuideIds.isEmpty()) {
            return List.of();
        }

        // Create a map for quick lookup of a guide's available time slots.
        Map<Long, List<LocalTime>> slotMap = availableSlots.stream()
                .collect(Collectors.toMap(AvailabilitySlot::getGuideId, AvailabilitySlot::getSlots));

        // Step 2: Fetch the full profiles of the available guides from MySQL,
        // filtering by location and language.
        List<Guide> matchingGuides = guideRepository.findAllByIdInAndLocationAndLanguagesContaining(
                availableGuideIds, location, language
        );

        // Step 3: Combine the profile and availability info into the final DTO.
        return matchingGuides.stream()
                // Only include guides who are fully approved.
                .filter(guide -> guide.getVerificationStatus() == Guide.VerificationStatus.APPROVED)
                .map(guide -> convertToSearchResultDto(guide, slotMap.get(guide.getId())))
                .collect(Collectors.toList());
    }

    // --- Utility Method ---
    private GuideSearchResultDto convertToSearchResultDto(Guide guide, List<LocalTime> slots) {
        return GuideSearchResultDto.builder()
                .id(guide.getId())
                .name(guide.getName())
                .profilePictureUrl(guide.getProfilePictureUrl())
                .bio(guide.getBio())
                .languages(guide.getLanguages())
                .location(guide.getLocation())
                .ratePerHour(guide.getRatePerHour())
                .verificationStatus(guide.getVerificationStatus())
                .availableSlots(slots)
                .build();
    }
}