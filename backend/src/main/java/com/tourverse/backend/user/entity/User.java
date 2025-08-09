
package com.tourverse.backend.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Audit fields
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	// User fields

	@Column(nullable = false, length = 100)
	@NotBlank(message = "Name is required")
	private String name;

	@Column(nullable = false, unique = true, length = 150)
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@Column(nullable = false, length = 10)
	@Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
	@NotBlank(message = "Phone number is required")
	private String phone;

	@Column(nullable = false, length = 200)
	@NotBlank(message = "Password is required")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;

	@Column(nullable = false)
	@Builder.Default
	private boolean isEnabled = true;
	
	@Column(name = "average_rating")
	@Builder.Default
	private double averageRating = 0.0;

	@Column(name = "rating_count")
	@Builder.Default
	private int ratingCount = 0;

	@Column(name = "profile_picture_url", length = 500)
	private String profilePictureUrl;

	public enum Role {
		TRAVELER, GUIDE, ADMIN
	}

}
