
package com.tourverse.backend.auth.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	private String oldPassword;
	
	@NotBlank(message = "New password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", 
	         message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
	private String newPassword;
	
	@NotBlank(message = "OTP is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
	private String otp;
}
