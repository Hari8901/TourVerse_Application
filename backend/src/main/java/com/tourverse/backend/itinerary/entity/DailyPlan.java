package com.tourverse.backend.itinerary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Link back to the main itinerary.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itinerary_id", nullable = false)
	@JsonIgnore // Prevents infinite loops during serialization
	private CustomItinerary itinerary;

	@Column(nullable = false)
	@NotNull(message = "Day number is required")
	private int dayNumber;

	@Column(nullable = false, length = 2000)
	@NotBlank(message = "The plan description is required")
	private String description;
}