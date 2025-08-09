package com.tourverse.backend.itinerary.entity;

import com.tourverse.backend.user.entity.Traveler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "custom_itineraries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomItinerary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traveler_id", nullable = false)
	private Traveler traveler;

	@Column(nullable = false, length = 200)
	@NotBlank(message = "Itinerary title is required")
	private String title;

	// A list of daily plans, linked to this itinerary.
	// CascadeType.ALL means if we save or delete an itinerary, its daily plans are
	// also saved/deleted.
	@OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<DailyPlan> dailyPlans;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;
}