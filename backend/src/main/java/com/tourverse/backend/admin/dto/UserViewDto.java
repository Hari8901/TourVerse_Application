package com.tourverse.backend.admin.dto;

import com.tourverse.backend.user.entity.User.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserViewDto {
	private Long id;
	private String name;
	private String email;
	private Role role;
	private boolean isEnabled;
}