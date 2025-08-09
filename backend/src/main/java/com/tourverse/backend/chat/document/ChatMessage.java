package com.tourverse.backend.chat.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Data
@Builder
public class ChatMessage {

	@Id
	private String id;

	// A unique identifier for the chat room, combining traveler and guide IDs.
	@Field("chat_room_id")
	private String chatRoomId;

	@Field("sender_id")
	private Long senderId;

	@Field("recipient_id")
	private Long recipientId;

	@Field("content")
	private String content;

	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
}