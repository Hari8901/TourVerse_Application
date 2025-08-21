package com.tourverse.backend.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ChangePasswordRequest {
	
	@NotBlank(message = "Old password is required")
    private String oldPassword;
    
    @NotBlank(message = "New password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", 
	         message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    private String newPassword;
}
