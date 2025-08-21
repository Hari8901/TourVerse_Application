package com.tourverse.backend.admin.dto;

import lombok.Data;

@Data
public class AdminRegisterRequest {
	private String name;
	private String email;
	private String phone;
	private String password;
	private String employeeId;
	private String department;
	private String otp;
}
