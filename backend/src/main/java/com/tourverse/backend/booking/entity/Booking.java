package com.tourverse.backend.booking.entity;

import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.user.entity.Traveler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	public enum BookingStatus {
		PENDING, CONFIRMED, CANCELED, COMPLETED
	}

	public enum PaymentStatus {
		UNPAID, PAID
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traveler_id", nullable = false)
	private Traveler traveler;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guide_id") // Nullable until a guide accepts
	private Guide guide;

	@Column(nullable = false)
	@NotNull(message = "Tour date is required")
	private LocalDate tourDate;

	@Column(nullable = false)
	@NotNull(message = "Tour time is required")
	private LocalTime tourTime;

	@Column(nullable = false)
	@NotNull(message = "Number of hours is required")
	private int hours;

	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Total amount is required")
	private BigDecimal totalAmount;

	@Column(nullable = false)
	@NotBlank(message = "Location is required")
	private String location;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private BookingStatus status = BookingStatus.PENDING;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

	@Column(name = "razorpay_order_id")
	private String razorpayOrderId;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

}