package com.tourverse.backend.itinerary.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ItineraryResponse {
	private Long id;
	private String title;
	private List<DailyPlanDto> dailyPlans;
}