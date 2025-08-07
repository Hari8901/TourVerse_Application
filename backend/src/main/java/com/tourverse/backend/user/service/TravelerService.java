package com.tourverse.backend.user.service;

import com.tourverse.backend.user.dto.TravelerDto;
import com.tourverse.backend.user.dto.TravelerUpdateRequest;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelerService {

	private final TravelerRepository travelerRepository;
	private final S3FileUploadService s3FileUploadService;

	public TravelerDto getProfile(Long userId) {
		Traveler traveler = travelerRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Traveler not found"));
		return convertToDto(traveler);
	}

	public TravelerDto updateProfile(Long userId, TravelerUpdateRequest req) {
		Traveler traveler = travelerRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Traveler not found"));

		if (req.getName() != null) {
			traveler.setName(req.getName());
		}
		if (req.getPhone() != null) {
			traveler.setPhone(req.getPhone());
		}
		if (req.getProfilePicture() != null && !req.getProfilePicture().isEmpty()) {
			
			String url = s3FileUploadService.uploadFile(req.getProfilePicture());
			traveler.setProfilePictureUrl(url);
			
		}
		travelerRepository.save(traveler);
		return convertToDto(traveler);
	}

	private TravelerDto convertToDto(Traveler traveler) {
		return TravelerDto.builder()
				.id(traveler.getId())
				.name(traveler.getName())
				.email(traveler.getEmail())
				.phone(traveler.getPhone())
				.profilePictureUrl(traveler.getProfilePictureUrl())
				.build();
	}
}