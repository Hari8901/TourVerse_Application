package com.tourverse.backend.user.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelerUpdateRequest {
	
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
	private String name;
	
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
	private String phone;
	
	private MultipartFile profilePicture;
}
