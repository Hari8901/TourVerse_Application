package com.tourverse.backend.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "travelers")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Traveler extends User {
	// No direct List<BookingEntity>; bookings loaded explicitly to reduce loading list overhead
}
