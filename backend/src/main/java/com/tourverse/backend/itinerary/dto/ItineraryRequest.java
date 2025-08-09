package com.tourverse.backend.itinerary.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItineraryRequest {
	private String title;
	private List<DailyPlanDto> dailyPlans;
}