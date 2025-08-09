package com.tourverse.backend.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tourverse.backend.common.util.AppConstants;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final long OTP_VALIDITY_MINUTES = 5;

	public OtpService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String generateOtp(String key) {
		String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
		redisTemplate.opsForValue().set(AppConstants.OTP_PREFIX + key, otp, OTP_VALIDITY_MINUTES, TimeUnit.MINUTES);
		return otp;
	}

	public boolean validateOtp(String key, String otp) {
		String cachedOtp = (String) redisTemplate.opsForValue().get(AppConstants.OTP_PREFIX + key);
		return otp != null && otp.equals(cachedOtp);
	}

	public void clearOtp(String key) {
		redisTemplate.delete(AppConstants.OTP_PREFIX + key);
	}
}