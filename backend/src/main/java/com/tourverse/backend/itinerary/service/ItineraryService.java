package com.tourverse.backend.itinerary.service;

import com.tourverse.backend.itinerary.dto.DailyPlanDto;
import com.tourverse.backend.itinerary.dto.ItineraryRequest;
import com.tourverse.backend.itinerary.dto.ItineraryResponse;
import com.tourverse.backend.itinerary.entity.CustomItinerary;
import com.tourverse.backend.itinerary.entity.DailyPlan;
import com.tourverse.backend.itinerary.repository.CustomItineraryRepository;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final CustomItineraryRepository itineraryRepository;
    private final TravelerRepository travelerRepository;

    @Transactional
    public ItineraryResponse createItinerary(Long travelerId, ItineraryRequest request) {
        Traveler traveler = travelerRepository.findById(travelerId)
                .orElseThrow(() -> new RuntimeException("Traveler not found"));

        CustomItinerary itinerary = CustomItinerary.builder()
                .traveler(traveler)
                .title(request.getTitle())
                .build();

        List<DailyPlan> dailyPlans = request.getDailyPlans().stream()
                .map(dto -> DailyPlan.builder()
                        .itinerary(itinerary) // Link back to the parent itinerary
                        .dayNumber(dto.getDayNumber())
                        .description(dto.getDescription())
                        .build())
                .collect(Collectors.toList());

        itinerary.setDailyPlans(dailyPlans);
        CustomItinerary savedItinerary = itineraryRepository.save(itinerary);
        return convertToResponseDto(savedItinerary);
    }

    @Transactional(readOnly = true)
    public List<ItineraryResponse> getMyItineraries(Long travelerId) {
        return itineraryRepository.findByTravelerId(travelerId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteItinerary(Long travelerId, Long itineraryId) {
        CustomItinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        // Security check: ensure the user owns this itinerary before deleting
        if (!itinerary.getTraveler().getId().equals(travelerId)) {
            throw new IllegalStateException("You are not authorized to delete this itinerary.");
        }
        itineraryRepository.delete(itinerary);
    }
    
    /**
     * Updates an existing custom itinerary.
     *
     * @param travelerId  The ID of the traveler who owns the itinerary.
     * @param itineraryId The ID of the itinerary to update.
     * @param request     The DTO containing the new title and daily plans.
     * @return A DTO of the updated itinerary.
     */
    @Transactional
    public ItineraryResponse updateItinerary(Long travelerId, Long itineraryId, ItineraryRequest request) {
        CustomItinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        // Security check: Ensure the user owns this itinerary.
        if (!itinerary.getTraveler().getId().equals(travelerId)) {
            throw new IllegalStateException("You are not authorized to update this itinerary.");
        }

        // Update the title
        itinerary.setTitle(request.getTitle());

        // Clear the old daily plans to replace them with the new set.
        // The 'orphanRemoval = true' in the entity mapping will handle deleting them from the database.
        itinerary.getDailyPlans().clear();

        // Add the new daily plans from the request
        List<DailyPlan> newDailyPlans = request.getDailyPlans().stream()
                .map(dto -> DailyPlan.builder()
                        .itinerary(itinerary)
                        .dayNumber(dto.getDayNumber())
                        .description(dto.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        itinerary.getDailyPlans().addAll(newDailyPlans);

        CustomItinerary updatedItinerary = itineraryRepository.save(itinerary);
        return convertToResponseDto(updatedItinerary);
    }

    // --- Utility Method ---
    private ItineraryResponse convertToResponseDto(CustomItinerary itinerary) {
        List<DailyPlanDto> dailyPlanDtos = itinerary.getDailyPlans().stream()
                .map(plan -> {
                    DailyPlanDto dto = new DailyPlanDto();
                    dto.setDayNumber(plan.getDayNumber());
                    dto.setDescription(plan.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());

        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .title(itinerary.getTitle())
                .dailyPlans(dailyPlanDtos)
                .build();
    }
}