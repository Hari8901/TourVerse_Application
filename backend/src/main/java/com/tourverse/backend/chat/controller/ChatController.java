package com.tourverse.backend.chat.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.chat.document.ChatMessage;
import com.tourverse.backend.chat.dto.ChatMessageDto;
import com.tourverse.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller // Use @Controller, not @RestController, for WebSocket message mappings
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * Handles incoming WebSocket messages sent to the "/app/chat" destination.
	 *
	 * @param auth    The authenticated user sending the message.
	 * @param request The DTO containing the message payload.
	 */
	@MessageMapping("/chat")
	public void processMessage(Authentication auth, @Payload ChatMessageDto request) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		chatService.sendMessage(principal.getUser().getId(), request);
	}

	/**
	 * REST endpoint for fetching the chat history between the authenticated user
	 * and another user.
	 *
	 * @param auth        The authenticated user requesting the history.
	 * @param otherUserId The ID of the other user in the conversation.
	 * @return A list of all messages in their chat history.
	 */
	@GetMapping("/history/{otherUserId}")
	public ResponseEntity<List<ChatMessage>> getChatHistory(Authentication auth, @PathVariable Long otherUserId) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		List<ChatMessage> history = chatService.getChatHistory(principal.getUser().getId(), otherUserId);
		return ResponseEntity.ok(history);
	}
}