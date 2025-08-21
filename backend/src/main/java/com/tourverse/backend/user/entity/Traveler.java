
package com.tourverse.backend.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "travelers")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Traveler extends User {
	// No direct List<BookingEntity>; bookings loaded explicitly to reduce loading
	// list overhead
}
