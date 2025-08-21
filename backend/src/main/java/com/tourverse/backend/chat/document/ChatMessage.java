package com.tourverse.backend.chat.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chat_messages")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
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