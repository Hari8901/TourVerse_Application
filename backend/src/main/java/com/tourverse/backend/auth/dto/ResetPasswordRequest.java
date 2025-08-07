
package com.tourverse.backend.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
	private String email;
	private String oldPassword;
	private String newPassword;
	private String otp;
}
