package com.tourverse.backend.tourPackage.services;


import com.tourverse.backend.tourPackage.dto.CreateTourPackageRequest;
import com.tourverse.backend.tourPackage.dto.TourPackageDto;
import com.tourverse.backend.tourPackage.entity.TourPackage;
import com.tourverse.backend.tourPackage.repository.TourPackageRepository;
import com.tourverse.backend.user.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourPackageService {

    private final TourPackageRepository tourPackageRepository;
    private final S3FileUploadService s3FileUploadService;

    // --- Admin-Only Operations ---

    /**
     * Creates a new tour package. This is an admin-only operation.
     */
    @Transactional
    public TourPackageDto createPackage(CreateTourPackageRequest request) {
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imageUrl = s3FileUploadService.uploadFile(request.getImage());
        }

        TourPackage tourPackage = TourPackage.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .durationDays(request.getDurationDays())
                .price(request.getPrice())
                .imageUrl(imageUrl)
                .inclusions(request.getInclusions())
                .build();

        TourPackage savedPackage = tourPackageRepository.save(tourPackage);
        return convertToDto(savedPackage);
    }

    /**
     * Updates an existing tour package. This is an admin-only operation.
     */
    @Transactional
    public TourPackageDto updatePackage(Long packageId, CreateTourPackageRequest request) {
        TourPackage existingPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Tour package not found with ID: " + packageId));

        if (request.getTitle() != null) existingPackage.setTitle(request.getTitle());
        if (request.getDescription() != null) existingPackage.setDescription(request.getDescription());
        if (request.getLocation() != null) existingPackage.setLocation(request.getLocation());
        if (request.getDurationDays() > 0) existingPackage.setDurationDays(request.getDurationDays());
        if (request.getPrice() != null) existingPackage.setPrice(request.getPrice());
        if (request.getInclusions() != null) existingPackage.setInclusions(request.getInclusions());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String newImageUrl = s3FileUploadService.uploadFile(request.getImage());
            existingPackage.setImageUrl(newImageUrl);
        }

        TourPackage updatedPackage = tourPackageRepository.save(existingPackage);
        return convertToDto(updatedPackage);
    }

    /**
     * Deletes a tour package. This is an admin-only operation.
     */
    @Transactional
    public void deletePackage(Long packageId) {
        if (!tourPackageRepository.existsById(packageId)) {
            throw new RuntimeException("Tour package not found with ID: " + packageId);
        }
        tourPackageRepository.deleteById(packageId);
    }

    // --- Public Operations ---

    /**
     * Retrieves all available tour packages.
     */
    @Transactional(readOnly = true)
    public List<TourPackageDto> getAllPackages() {
        return tourPackageRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single tour package by its ID.
     */
    @Transactional(readOnly = true)
    public TourPackageDto getPackageById(Long packageId) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Tour package not found with ID: " + packageId));
        return convertToDto(tourPackage);
    }

    // --- Utility Method ---

    private TourPackageDto convertToDto(TourPackage tourPackage) {
        return TourPackageDto.builder()
                .id(tourPackage.getId())
                .title(tourPackage.getTitle())
                .description(tourPackage.getDescription())
                .location(tourPackage.getLocation())
                .durationDays(tourPackage.getDurationDays())
                .price(tourPackage.getPrice())
                .imageUrl(tourPackage.getImageUrl())
                .inclusions(tourPackage.getInclusions())
                .build();
    }
}