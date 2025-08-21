package com.tourverse.backend.guide.service;

import com.tourverse.backend.auth.dto.ChangePasswordRequest;
import com.tourverse.backend.auth.dto.OtpVerificationRequest;
import com.tourverse.backend.auth.service.EmailService;
import com.tourverse.backend.auth.service.JwtTokenService;
import com.tourverse.backend.auth.service.OtpService;
import com.tourverse.backend.common.exceptions.UserNotFoundException;
import com.tourverse.backend.guide.dto.GuideDto;
import com.tourverse.backend.guide.dto.GuideLoginRequest;
import com.tourverse.backend.guide.dto.GuideRegisterRequest;
import com.tourverse.backend.guide.dto.GuideUpdateRequest;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.entity.Guide.VerificationStatus;
import com.tourverse.backend.guide.repository.GuideRepository;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.UserRepository;
import com.tourverse.backend.user.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GuideAuthService {

	private final UserRepository userRepository;
	private final GuideRepository guideRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3FileUploadService s3FileUploadService;
	private final OtpService otpService;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;

	// --- REGISTRATION ---

	@Transactional
	public void initiateRegistration(GuideRegisterRequest req) {
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException("Email is already registered.");
		}
		String otp = otpService.generateOtp(req.getEmail());
		emailService.sendOtpEmail(req.getEmail(), otp, "Complete Your TourVerse Guide Registration");
	}

	@Transactional
	public void verifyAndCompleteRegistration(GuideRegisterRequest req) {
		if (!otpService.validateOtp(req.getEmail(), req.getOtp())) {
			throw new IllegalArgumentException("Invalid or expired OTP.");
		}
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new IllegalStateException("User is already registered.");
		}

		String profilePictureUrl = uploadFile(req.getProfilePicture());
		String aadhaarUrl = uploadFile(req.getAadhaarDocument());
		String panUrl = uploadFile(req.getPanDocument());
		String certificateUrl = uploadFile(req.getGuideCertificate());

		Guide guide = Guide
				.builder()
				.name(req.getName())
				.email(req.getEmail())
				.phone(req.getPhone())
				.password(passwordEncoder.encode(req.getPassword()))
				.role(User.Role.GUIDE)
				.profilePictureUrl(profilePictureUrl)
				.aadhaarNumber(req.getAadhaarNumber())
				.panNumber(req.getPanNumber())
				.bio(req.getBio())
				.languages(req.getLanguages())
				.ratePerHour(req.getRatePerHour())
				.location(req.getLocation())
				.aadhaarDocumentUrl(aadhaarUrl)
				.panDocumentUrl(panUrl)
				.guideCertificateUrl(certificateUrl)
				.verificationStatus(VerificationStatus.PENDING)
				.build();

		guideRepository.save(guide);
		otpService.clearOtp(req.getEmail());
	}

	// --- LOGIN & LOGOUT ---

	public void loginInit(GuideLoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

		String otp = otpService.generateOtp(req.getEmail());
		emailService.sendOtpEmail(req.getEmail(), otp, "Your TourVerse Login OTP");
	}

	public String loginVerify(OtpVerificationRequest req) {
		if (!otpService.validateOtp(req.getEmail(), req.getOtp())) {
			throw new BadCredentialsException("Invalid OTP.");
		}
		User user = userRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		otpService.clearOtp(req.getEmail());
		return jwtTokenService.generateToken(user);
	}

	public void logout(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			jwtTokenService.blacklistToken(token);
		}
	}

	// --- PASSWORD MANAGEMENT ---

	public void sendPasswordResetOtp(String email) {
		userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
		String otp = otpService.generateOtp(email);
		emailService.sendOtpEmail(email, otp, "Your TourVerse Password Reset OTP");
	}

	@Transactional
	public void resetPassword(String email, String newPassword, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		if (!otpService.validateOtp(email, otp)) {
			throw new BadCredentialsException("Invalid or expired OTP.");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		otpService.clearOtp(email);
	}
	
	@Transactional
	public void changePassword(Long userId, ChangePasswordRequest req) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect old password.");
		}

		user.setPassword(passwordEncoder.encode(req.getNewPassword()));
		userRepository.save(user);
	}

	// --- PROFILE MANAGEMENT ---

	@Transactional(readOnly = true)
	public GuideDto getDashboard(Long userId) {
		Guide guide = guideRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Guide not found with ID: " + userId));
		return convertToDto(guide);
	}

	@Transactional
	public GuideDto updateProfile(Long userId, GuideUpdateRequest req) {
		Guide guide = guideRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Guide not found with ID: " + userId));

		if (req.getName() != null)
			guide.setName(req.getName());
		if (req.getPhone() != null)
			guide.setPhone(req.getPhone());
		if (req.getBio() != null)
			guide.setBio(req.getBio());
		if (req.getLanguages() != null)
			guide.setLanguages(req.getLanguages());
		if (req.getRatePerHour() != null)
			guide.setRatePerHour(req.getRatePerHour());
		if (req.getLocation() != null)
			guide.setLocation(req.getLocation());

		if (req.getProfilePicture() != null && !req.getProfilePicture().isEmpty()) {
			String url = s3FileUploadService.uploadFile(req.getProfilePicture());
			guide.setProfilePictureUrl(url);
		}

		guideRepository.save(guide);
		return convertToDto(guide);
	}

	@Transactional
	public void deleteGuide(Long userId) {
		if (!guideRepository.existsById(userId)) {
			throw new UserNotFoundException("Guide not found with ID: " + userId);
		}
		guideRepository.deleteById(userId);
	}

	// --- UTILITY METHODS ---

	private String uploadFile(MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			return s3FileUploadService.uploadFile(file);
		}
		return null;
	}

	private GuideDto convertToDto(Guide guide) {
		return GuideDto
				.builder()
				.id(guide.getId())
				.name(guide.getName())
				.email(guide.getEmail())
				.phone(guide.getPhone())
				.profilePictureUrl(guide.getProfilePictureUrl())
				.bio(guide.getBio())
				.languages(guide.getLanguages())
				.ratePerHour(guide.getRatePerHour())
				.location(guide.getLocation())
				.verificationStatus(guide.getVerificationStatus())
				.build();
	}
}