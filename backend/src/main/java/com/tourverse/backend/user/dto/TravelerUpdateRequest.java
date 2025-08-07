package com.tourverse.backend.user.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelerUpdateRequest {
	private String name;
	private String phone;
	private MultipartFile profilePicture;
}
