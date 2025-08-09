package com.tourverse.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
		// Enables a simple in-memory message broker to carry messages back to the
		// client
		// on destinations prefixed with "/topic" and "/user".
		registry.enableSimpleBroker("/topic", "/user");

		// Designates the "/app" prefix for messages that are bound for
		// @MessageMapping-annotated methods.
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
		// Registers the "/ws" endpoint, enabling SockJS fallback options so that
		// alternate
		// transports can be used if WebSocket is not available.
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	}
}