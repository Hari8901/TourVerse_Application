package com.tourverse.backend.chat.repository;

import com.tourverse.backend.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	// Finds all messages for a specific chat room, ordered by time.
	List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);
}