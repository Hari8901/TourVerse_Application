package com.tourverse.backend.booking.repository;

import com.tourverse.backend.booking.entity.PackageBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageBookingRepository extends JpaRepository<PackageBooking, Long> {

}