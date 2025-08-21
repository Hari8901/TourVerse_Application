
package com.tourverse.backend.auth.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerificationRequest {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	@NotBlank(message = "OTP is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
	private String otp;
}
