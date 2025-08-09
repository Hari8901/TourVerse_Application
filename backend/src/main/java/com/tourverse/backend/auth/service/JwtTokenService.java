package com.tourverse.backend.auth.service;

import com.tourverse.backend.auth.util.JwtUtil;
import com.tourverse.backend.common.util.AppConstants;
import com.tourverse.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final UserDetailsService userDetailsService;

	public String generateToken(User user) {
		return jwtUtil.generateToken(user);
	}

	public boolean isTokenValid(String token) {
		// This method is now more robust and aligns with SecurityConfig
		try {
			final String username = getEmailFromToken(token);
			if (username == null)
				return false;
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			return jwtUtil.validateToken(token, userDetails) && !isTokenBlacklisted(token);
		} catch (Exception e) {
			return false;
		}
	}

	public String getEmailFromToken(String token) {
		return jwtUtil.extractUsername(token);
	}

	public String getRoleFromToken(String token) {
		// Correctly extract the 'role' claim
		return jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
	}

	public Long getUserIdFromToken(String token) {
		// Correctly extract the 'userId' claim
		return jwtUtil.extractClaim(token, claims -> claims.get("userId", Long.class));
	}

	public boolean isTokenBlacklisted(String token) {
		// No changes needed here, this is correct
		return redisTemplate.opsForValue().get(AppConstants.BLACKLIST_PREFIX + token) != null;
	}

	public void blacklistToken(String token) {
		// Use the token's actual expiration for the blacklist duration
		Date expiration = jwtUtil.extractExpiration(token);
		long durationMillis = expiration.getTime() - System.currentTimeMillis();
		if (durationMillis > 0) {
			redisTemplate.opsForValue().set(AppConstants.BLACKLIST_PREFIX + token, true, durationMillis,
					TimeUnit.MILLISECONDS);
		}
	}
}