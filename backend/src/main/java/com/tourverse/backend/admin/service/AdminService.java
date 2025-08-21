package com.tourverse.backend.admin.service;

import com.tourverse.backend.admin.dto.AdminRegisterRequest;
import com.tourverse.backend.admin.dto.GuideVerificationDto;
import com.tourverse.backend.admin.dto.UserViewDto;
import com.tourverse.backend.admin.entity.Admin;
import com.tourverse.backend.admin.repository.AdminRepository;
import com.tourverse.backend.auth.dto.OtpVerificationRequest;
import com.tourverse.backend.auth.service.EmailService;
import com.tourverse.backend.auth.service.JwtTokenService;
import com.tourverse.backend.auth.service.OtpService;
import com.tourverse.backend.common.exceptions.UserNotFoundException;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.repository.GuideRepository;
import com.tourverse.backend.user.dto.TravelerLoginRequest;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtTokenService;
	private final AuthenticationManager authenticationManager;
	private final OtpService otpService;
	private final EmailService emailService;
	private final AdminRepository adminRepository;
	private final UserRepository userRepository;
	private final GuideRepository guideRepository;
	private final TravelerRepository travelerRepository;

	// --- REGISTRATION ---
	@Transactional
	public void initiateRegistration(AdminRegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email is already registered.");
		}
		String otp = otpService.generateOtp(request.getEmail());
		emailService.sendOtpEmail(request.getEmail(), otp, "Your TourVerse Admin Registration OTP");
	}

	@Transactional
	public void completeRegistration(AdminRegisterRequest request) {
		if (!otpService.validateOtp(request.getEmail(), request.getOtp())) {
			throw new BadCredentialsException("Invalid or expired OTP.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalStateException("User is already registered.");
		}
		Admin admin = Admin.builder().name(request.getName()).email(request.getEmail()).phone(request.getPhone())
				.password(passwordEncoder.encode(request.getPassword())).role(User.Role.ADMIN)
				.employeeId(request.getEmployeeId()).department(request.getDepartment()).build();
		userRepository.save(admin);
		otpService.clearOtp(request.getEmail());
	}

	// --- LOGIN ---
	public void loginInit(TravelerLoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
		String otp = otpService.generateOtp(req.getEmail());
		emailService.sendOtpEmail(req.getEmail(), otp, "Your TourVerse Admin Login OTP");
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

	// --- PASSWORD MANAGEMENT ---
	@Transactional
	public void changePassword(Long adminId, String oldPassword, String newPassword) {
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new UserNotFoundException("Admin not found with ID: " + adminId));

		if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
			throw new BadCredentialsException("Incorrect old password.");
		}
		admin.setPassword(passwordEncoder.encode(newPassword));
		adminRepository.save(admin);
	}

	public void sendPasswordResetOtp(String email) {
		userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
		String otp = otpService.generateOtp(email);
		emailService.sendOtpEmail(email, otp, "Your TourVerse Password Reset OTP");
	}

	@Transactional
	public void resetPasswordWithOtp(String email, String newPassword, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		if (!otpService.validateOtp(email, otp)) {
			throw new BadCredentialsException("Invalid or expired OTP.");
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		otpService.clearOtp(email);
	}

	// --- Guide Verification ---

	@Transactional(readOnly = true)
	public List<GuideVerificationDto> getUnverifiedGuides() {
		return guideRepository.findByVerificationStatus(Guide.VerificationStatus.PENDING).stream()
				.map(this::convertToGuideVerificationDto).collect(Collectors.toList());
	}

	@Transactional
	public void verifyGuide(Long guideId, boolean approve) {
		Guide guide = guideRepository.findById(guideId)
				.orElseThrow(() -> new UserNotFoundException("Guide not found with ID: " + guideId));

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
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
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