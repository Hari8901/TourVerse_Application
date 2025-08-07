
package com.tourverse.backend.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelerLoginRequest {

	private String email;
	private String password;
	private String otp;
}
