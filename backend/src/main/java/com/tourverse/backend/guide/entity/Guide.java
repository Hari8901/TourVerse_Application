package com.tourverse.backend.guide.entity;

import com.tourverse.backend.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "guides", uniqueConstraints = { @UniqueConstraint(columnNames = "aadhaar_no"),
		@UniqueConstraint(columnNames = "pan_no") })
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Guide extends User {

	@Column(name = "aadhaar_no", nullable = false, length = 12)
	@Size(min = 12, max = 12, message = "Aadhaar number must be exactly 12 digits")
	@NotBlank(message = "Aadhaar number is required")
	private String aadhaarNumber;

	@Column(name = "pan_no", nullable = false, length = 10)
	@Size(min = 10, max = 10, message = "PAN number must be exactly 10 characters")
	@NotBlank(message = "PAN number is required")
	private String panNumber;

	@Column(name = "biography", length = 1000)
	private String bio;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "guide_languages", joinColumns = @JoinColumn(name = "guide_id"))
	@Column(name = "language", nullable = false)
	@NotEmpty(message = "At least one language must be provided")
	@Builder.Default
	private List<String> languages = Collections.emptyList();

	@Column(name = "rate_per_hour", nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Rate per hour is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Rate per hour must be positive")
	private BigDecimal ratePerHour;

	@Column(nullable = false)
	@NotBlank(message = "Location is required")
	private String location;

	public enum VerificationStatus { PENDING, APPROVED, REJECTED }

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private VerificationStatus verificationStatus = VerificationStatus.PENDING;

	@Column(name = "aadhaar_document_url", nullable = false)
	@NotBlank(message = "Aadhaar document URL is required")
	private String aadhaarDocumentUrl;

	@Column(name = "pan_document_url", nullable = false)
	@NotBlank(message = "PAN document URL is required")
	private String panDocumentUrl;

	@Column(name = "guide_certificate_url")
	private String guideCertificateUrl;
}