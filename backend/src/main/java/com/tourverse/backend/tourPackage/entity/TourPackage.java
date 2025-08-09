package com.tourverse.backend.tourPackage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tour_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	@NotBlank(message = "Package title is required")
	private String title;

	@Column(nullable = false, length = 2000)
	@NotBlank(message = "Description is required")
	private String description;

	@Column(nullable = false)
	@NotBlank(message = "Location is required")
	private String location;

	@Column(nullable = false)
	@NotNull(message = "Duration in days is required")
	private int durationDays;

	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
	private BigDecimal price;

	@Column(name = "image_url", length = 500)
	private String imageUrl;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "tour_package_inclusions", joinColumns = @JoinColumn(name = "package_id"))
	@Column(name = "inclusion", nullable = false)
	private List<String> inclusions; // e.g., "Hotel Stay", "Breakfast", "Airport Transfer"

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;
}