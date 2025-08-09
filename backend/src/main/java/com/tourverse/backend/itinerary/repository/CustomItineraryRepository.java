package com.tourverse.backend.itinerary.repository;

import com.tourverse.backend.itinerary.entity.CustomItinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomItineraryRepository extends JpaRepository<CustomItinerary, Long> {
	// Finds all itineraries created by a specific traveler
	List<CustomItinerary> findByTravelerId(Long travelerId);
}