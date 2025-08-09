package com.tourverse.backend.user.service;

import com.tourverse.backend.auth.service.EmailService;
import com.tourverse.backend.auth.service.JwtTokenService;
import com.tourverse.backend.auth.service.OtpService;
import com.tourverse.backend.user.dto.TravelerDto;
import com.tourverse.backend.user.dto.TravelerLoginRequest;
import com.tourverse.backend.user.dto.TravelerRegisterRequest;
import com.tourverse.backend.user.dto.TravelerUpdateRequest;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.TravelerRepository;
import com.tourverse.backend.user.repository.UserRepository;
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
public class TravelerAuthService {

	private final UserRepository userRepository;
	private final TravelerRepository travelerRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3FileUploadService s3FileUploadService;
	private final OtpService otpService;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;

	// --- REGISTRATION ---

	@Transactional
	public void initiateRegistration(TravelerRegisterRequest req) {
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException("Email is already registered.");
		}
		String otp = otpService.generateOtp(req.getEmail());
		emailService.sendOtpEmail(req.getEmail(), otp, "Complete Your TourVerse Registration");
	}

	@Transactional
	public void verifyAndCompleteRegistration(TravelerRegisterRequest req) {
		if (!otpService.validateOtp(req.getEmail(), req.getOtp())) {
			throw new IllegalArgumentException("Invalid or expired OTP.");
		}
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new IllegalStateException("User is already registered.");
		}

		String profilePictureUrl = uploadFile(req.getProfilePicture());

		Traveler traveler = Traveler.builder().name(req.getName()).email(req.getEmail()).phone(req.getPhone())
				.password(passwordEncoder.encode(req.getPassword())).role(User.Role.TRAVELER)
				.profilePictureUrl(profilePictureUrl).build();

		travelerRepository.save(traveler);
		otpService.clearOtp(req.getEmail());
	}

	// --- LOGIN & LOGOUT ---

	public String login(TravelerLoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

		if (req.getOtp() == null || req.getOtp().isBlank()) {
			String otp = otpService.generateOtp(req.getEmail());
			emailService.sendOtpEmail(req.getEmail(), otp, "Your TourVerse Login OTP");
			return null;
		}

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
	public void resetPassword(String email, String newPassword, String oldPassword, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		if (oldPassword != null && !oldPassword.isBlank()) {
			if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
				throw new BadCredentialsException("Incorrect old password.");
			}
		} else if (otp != null && !otp.isBlank()) {
			if (!otpService.validateOtp(email, otp)) {
				throw new BadCredentialsException("Invalid or expired OTP.");
			}
			otpService.clearOtp(email);
		} else {
			throw new IllegalArgumentException("Provide old password or OTP.");
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	// --- PROFILE MANAGEMENT ---

	@Transactional(readOnly = true)
	public TravelerDto getDashboard(Long userId) {
		Traveler traveler = travelerRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Traveler not found"));
		return convertToDto(traveler);
	}

	@Transactional
	public TravelerDto updateProfile(Long userId, TravelerUpdateRequest req) {
		Traveler traveler = travelerRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Traveler not found"));

		if (req.getName() != null)
			traveler.setName(req.getName());
		if (req.getPhone() != null)
			traveler.setPhone(req.getPhone());

		if (req.getProfilePicture() != null && !req.getProfilePicture().isEmpty()) {
			String url = s3FileUploadService.uploadFile(req.getProfilePicture());
			traveler.setProfilePictureUrl(url);
		}

		travelerRepository.save(traveler);
		return convertToDto(traveler);
	}

	@Transactional
	public void deleteProfile(Long userId) {
		if (!travelerRepository.existsById(userId)) {
			throw new RuntimeException("Traveler not found");
		}
		travelerRepository.deleteById(userId);
	}

	// --- UTILITY METHODS ---

	private String uploadFile(MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			return s3FileUploadService.uploadFile(file);
		}
		return null;
	}

	private TravelerDto convertToDto(Traveler traveler) {
		return TravelerDto.builder().id(traveler.getId()).name(traveler.getName()).email(traveler.getEmail())
				.phone(traveler.getPhone()).profilePictureUrl(traveler.getProfilePictureUrl()).build();
	}
}