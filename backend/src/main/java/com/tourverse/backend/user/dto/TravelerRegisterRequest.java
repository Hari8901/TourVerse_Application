package com.tourverse.backend.user.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelerRegisterRequest {
	private String name;
	private String email;
	private String phone;
	private String password;
	private MultipartFile profilePicture;
	private String otp; // used only in verify step
}
