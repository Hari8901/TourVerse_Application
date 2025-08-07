package com.tourverse.backend.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelerDto {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String profilePictureUrl;
}
