package com.tourverse.backend.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tourverse.backend.user.entity.Traveler;

@Repository
public interface TravelerRepository extends JpaRepository<Traveler, Long> {
	Optional<Traveler> findByEmail(String email);
}
