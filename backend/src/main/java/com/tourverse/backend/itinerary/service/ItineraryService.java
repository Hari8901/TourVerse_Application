package com.tourverse.backend.itinerary.service;

import com.tourverse.backend.common.exceptions.ItineraryException;
import com.tourverse.backend.common.exceptions.UserNotFoundException;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final CustomItineraryRepository itineraryRepository;
    private final TravelerRepository travelerRepository;

    @Transactional
    public ItineraryResponse createItinerary(Long travelerId, ItineraryRequest request) {
        validateItineraryRequest(request);
        Traveler traveler = travelerRepository.findById(travelerId)
                .orElseThrow(() -> new UserNotFoundException("Traveler not found"));

        CustomItinerary itinerary = CustomItinerary.builder()
                .traveler(traveler)
                .title(request.getTitle())
                .build();

        List<DailyPlan> dailyPlans = request.getDailyPlans().stream()
                .map(dto -> DailyPlan.builder()
                        .itinerary(itinerary)
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
                .orElseThrow(() -> new ItineraryException("Itinerary not found"));

        if (!itinerary.getTraveler().getId().equals(travelerId)) {
            throw new IllegalStateException("You are not authorized to delete this itinerary.");
        }
        itineraryRepository.delete(itinerary);
    }
    @Transactional
    public ItineraryResponse updateItinerary(Long travelerId, Long itineraryId, ItineraryRequest request) {
        validateItineraryRequest(request);
        CustomItinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new ItineraryException("Itinerary not found"));

        if (!itinerary.getTraveler().getId().equals(travelerId)) {
            throw new IllegalStateException("You are not authorized to update this itinerary.");
        }

        itinerary.setTitle(request.getTitle());

        // Efficiently update daily plans
        Map<Integer, DailyPlan> existingPlansByDay = itinerary.getDailyPlans().stream()
                .collect(Collectors.toMap(DailyPlan::getDayNumber, Function.identity()));

        Map<Integer, DailyPlanDto> newPlansByDay = request.getDailyPlans().stream()
                .collect(Collectors.toMap(DailyPlanDto::getDayNumber, Function.identity()));

        // Remove plans that are no longer in the request
        itinerary.getDailyPlans().removeIf(plan -> !newPlansByDay.containsKey(plan.getDayNumber()));

        // Update existing plans and add new ones
        newPlansByDay.forEach((dayNumber, dto) -> {
            DailyPlan existingPlan = existingPlansByDay.get(dayNumber);
            if (existingPlan != null) {
                existingPlan.setDescription(dto.getDescription());
            } else {
                itinerary.getDailyPlans().add(DailyPlan.builder()
                        .itinerary(itinerary)
                        .dayNumber(dayNumber)
                        .description(dto.getDescription())
                        .build());
            }
        });

        CustomItinerary updatedItinerary = itineraryRepository.save(itinerary);
        return convertToResponseDto(updatedItinerary);
    }

    private void validateItineraryRequest(ItineraryRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ItineraryException("Itinerary title cannot be empty.");
        }
        if (CollectionUtils.isEmpty(request.getDailyPlans())) {
            throw new ItineraryException("Itinerary must have at least one daily plan.");
        }
        for (DailyPlanDto plan : request.getDailyPlans()) {
            if (plan.getDayNumber() <= 0) {
                throw new ItineraryException("Day number must be positive.");
            }
            if (plan.getDescription() == null || plan.getDescription().isBlank()) {
                throw new ItineraryException("Daily plan description cannot be empty.");
            }
        }
    }

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