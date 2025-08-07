package com.tourverse.backend.auth.service;

import com.tourverse.backend.user.dto.TravelerLoginRequest;
import com.tourverse.backend.user.dto.TravelerRegisterRequest;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.entity.User.Role;
import com.tourverse.backend.user.repository.TravelerRepository;
import com.tourverse.backend.user.repository.UserRepository;
import com.tourverse.backend.user.service.S3FileUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final TravelerRepository travelerRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final S3FileUploadService s3FileUploadService;
	private final JwtTokenService jwtTokenService;
	private final OtpService otpService;
	private final EmailService emailService;

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

		String profilePictureUrl = null;
		if (req.getProfilePicture() != null && !req.getProfilePicture().isEmpty()) {
			profilePictureUrl = s3FileUploadService.uploadFile(req.getProfilePicture());
		}

		Traveler traveler = Traveler.builder().name(req.getName()).email(req.getEmail()).phone(req.getPhone())
				.password(passwordEncoder.encode(req.getPassword())).role(Role.TRAVELER)
				.profilePictureUrl(profilePictureUrl).build();

		travelerRepository.save(traveler);
		otpService.clearOtp(req.getEmail()); // Clear the OTP after successful registration
	}

	public String login(TravelerLoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

		// If OTP is not provided, send one
		if (req.getOtp() == null || req.getOtp().isBlank()) {
			String otp = otpService.generateOtp(req.getEmail());
			emailService.sendOtpEmail(req.getEmail(), otp, "Your TourVerse Login OTP");
			return null; // Indicate that OTP is required
		}

		// If OTP is provided, validate it
		if (!otpService.validateOtp(req.getEmail(), req.getOtp())) {
			throw new BadCredentialsException("Invalid OTP.");
		}

		User user = userRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		otpService.clearOtp(req.getEmail()); // Clear OTP after successful login
		return jwtTokenService.generateToken(user);
	}

	public void logout(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			// Delegate blacklisting to the token service
			jwtTokenService.blacklistToken(token);
		}
	}

	public void sendPasswordResetOtp(String email) {
		userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
		String otp = otpService.generateOtp(email);
		emailService.sendOtpEmail(email, otp, "Your TourVerse Password Reset OTP");
	}

	@Transactional
	public void resetPassword(String email, String newPassword, String oldPassword, String otp) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		boolean passwordReset = false;

		// Case 1: Resetting with old password (from user profile settings)
		if (oldPassword != null && !oldPassword.isBlank()) {
			if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
				throw new BadCredentialsException("Incorrect old password.");
			}
			passwordReset = true;
		}
		// Case 2: Resetting with OTP (from "forgot password" flow)
		else if (otp != null && !otp.isBlank()) {
			if (!otpService.validateOtp(email, otp)) {
				throw new BadCredentialsException("Invalid or expired OTP.");
			}
			otpService.clearOtp(email); // Clear OTP after use
			passwordReset = true;
		}

		if (!passwordReset) {
			throw new IllegalArgumentException(
					"You must provide either an old password or an OTP to reset your password.");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}
}