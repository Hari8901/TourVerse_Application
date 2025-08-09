package com.tourverse.backend.tourPackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tourverse.backend.tourPackage.entity.TourPackage;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
}