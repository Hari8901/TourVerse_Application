package com.tourverse.backend.guide.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideLoginRequest {
	private String email;
	private String password;
	private String otp;
}
