package com.tourverse.backend.booking.entity;

import com.tourverse.backend.tourPackage.entity.TourPackage;
import com.tourverse.backend.user.entity.Traveler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "package_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageBooking {

	public enum PackageBookingStatus {
		PENDING_PAYMENT, CONFIRMED, CANCELED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traveler_id", nullable = false)
	private Traveler traveler;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", nullable = false)
	private TourPackage tourPackage;

	@Column(nullable = false)
	@NotNull(message = "Travel date is required")
	private LocalDate travelDate;

	@Column(nullable = false)
	@NotNull(message = "Number of travelers is required")
	private int numberOfTravelers;

	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Total amount is required")
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private PackageBookingStatus status = PackageBookingStatus.PENDING_PAYMENT;

	@Column(name = "razorpay_order_id")
	private String razorpayOrderId;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;
}