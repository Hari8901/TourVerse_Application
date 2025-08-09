package com.tourverse.backend.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
	private Long recipientId;
	private String content;
}