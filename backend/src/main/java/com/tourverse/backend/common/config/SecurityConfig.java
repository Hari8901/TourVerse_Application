package com.tourverse.backend.common.config;

import com.tourverse.backend.auth.service.JwtTokenService;
import com.tourverse.backend.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
				.authorizeHttpRequests(
						auth -> auth
						.requestMatchers("/api/auth/**", "/api/public/**", "/swagger-ui/**", "/api-docs/**")
						.permitAll()
						.requestMatchers("/api/traveler/**").hasRole("TRAVELER")
						.requestMatchers("/api/guide/**").hasRole("GUIDE")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.anyRequest()
						.authenticated())
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

	@RequiredArgsConstructor
	public static class JwtAuthenticationFilter extends OncePerRequestFilter {

		private final JwtUtil jwtUtil;
		private final UserDetailsService userDetailsService;
		private final JwtTokenService jwtTokenService;

		@Override
		protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
				@NonNull FilterChain filterChain) throws ServletException, IOException {

			final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			final String jwt;
			final String userEmail;

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			jwt = authHeader.substring(7);

			// Check if the token is blacklisted before proceeding
			if (jwtTokenService.isTokenBlacklisted(jwt)) {
				// You could send an unauthorized response here as well
				filterChain.doFilter(request, response);
				return;
			}

			try {
				userEmail = jwtUtil.extractUsername(jwt);

				if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

					if (jwtUtil.validateToken(jwt, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			} catch (Exception ex) {
				// Log the exception details
				logger.error("Could not set user authentication in security context", ex);
			}

			filterChain.doFilter(request, response);
		}
	}
}