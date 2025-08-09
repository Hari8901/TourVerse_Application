package com.tourverse.backend.admin.service;

import com.tourverse.backend.admin.dto.GuideVerificationDto;
import com.tourverse.backend.admin.dto.UserViewDto;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.repository.GuideRepository;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.TravelerRepository;
import com.tourverse.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	private final GuideRepository guideRepository;
	private final TravelerRepository travelerRepository;

	// --- Guide Verification ---

	@Transactional(readOnly = true)
	public List<GuideVerificationDto> getUnverifiedGuides() {
		return guideRepository.findByVerificationStatus(Guide.VerificationStatus.PENDING).stream()
				.map(this::convertToGuideVerificationDto).collect(Collectors.toList());
	}

	@Transactional
	public void verifyGuide(Long guideId, boolean approve) {
		Guide guide = guideRepository.findById(guideId).orElseThrow(() -> new RuntimeException("Guide not found..."));

		if (approve) {
			guide.setVerificationStatus(Guide.VerificationStatus.APPROVED);
		} else {
			guide.setVerificationStatus(Guide.VerificationStatus.REJECTED);
		}
		guideRepository.save(guide);
	}

	// --- User Management ---

	@Transactional(readOnly = true)
	public List<UserViewDto> getAllGuides() {
		return guideRepository.findAll().stream().map(this::convertToUserViewDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<UserViewDto> getAllTravelers() {
		return travelerRepository.findAll().stream().map(this::convertToUserViewDto).collect(Collectors.toList());
	}

	@Transactional
	public void blockUser(Long userId) {
		toggleUserStatus(userId, false);
	}

	@Transactional
	public void unblockUser(Long userId) {
		toggleUserStatus(userId, true);
	}

	private void toggleUserStatus(Long userId, boolean isEnabled) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
		user.setEnabled(isEnabled);
		userRepository.save(user);
	}

	// --- DTO Converters ---

	private UserViewDto convertToUserViewDto(User user) {
		return UserViewDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole())
				.isEnabled(user.isEnabled()).build();
	}

	private GuideVerificationDto convertToGuideVerificationDto(Guide guide) {
		return GuideVerificationDto.builder().id(guide.getId()).name(guide.getName()).email(guide.getEmail())
				.phone(guide.getPhone()).location(guide.getLocation()).languages(guide.getLanguages())
				.ratePerHour(guide.getRatePerHour()).aadhaarDocumentUrl(guide.getAadhaarDocumentUrl())
				.panDocumentUrl(guide.getPanDocumentUrl()).guideCertificateUrl(guide.getGuideCertificateUrl()).build();
	}
}