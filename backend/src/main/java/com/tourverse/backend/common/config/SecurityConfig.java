package com.tourverse.backend.common.config;

import com.tourverse.backend.auth.filter.JwtAuthenticationFilter;
import com.tourverse.backend.auth.service.JwtTokenService;
import com.tourverse.backend.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final JwtTokenService jwtTokenService;
	private final UserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth
	                    // Define SPECIFIC public endpoints
	                    .requestMatchers(
	                    		"/api/public/**",
	                            "/api/traveler/login",
	                            "/api/traveler/register/init",
	                            "/api/traveler/register/verify",
	                            "/api/traveler/forgot-password",
	                            "/api/traveler/reset-password",
	                            "/api/guide/login",
	                            "/api/guide/register/init",
	                            "/api/guide/register/verify",
	                            "/api/guide/forgot-password",
	                            "/api/guide/reset-password",
	                            "/swagger-ui/**",
	                            "/api-docs/**"
	                    ).permitAll()
	                    // Secure role-specific endpoints
	                    .requestMatchers("/api/chat/**").authenticated()
	                    .requestMatchers("/api/reviews").authenticated()
	                    .requestMatchers("/api/payment/**").authenticated()
	                    .requestMatchers("/api/bookings/**").authenticated()
	                    .requestMatchers("/api/guide/**").hasRole("GUIDE")
	                    .requestMatchers("/api/traveler/**").hasRole("TRAVELER")
	                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
	                    
	                    // All other requests must be authenticated
	                    .anyRequest().authenticated()
	            )
	            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedEntryPoint()))
	            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
	
	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {

		return new JwtAuthenticationFilter(jwtUtil, userDetailsService, jwtTokenService);
	}

	private AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");
			response.getWriter()
					.write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
		};
	}
}