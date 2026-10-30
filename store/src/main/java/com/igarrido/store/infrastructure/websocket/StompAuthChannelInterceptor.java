package com.igarrido.store.infrastructure.websocket;

import com.igarrido.store.infrastructure.security.CustomUserDetailsService;
import com.igarrido.store.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Valida el JWT enviado en la cabecera CONNECT (Authorization: Bearer ...) y
 * establece un Authentication como Principal para los mensajes STOMP posteriores.
 */
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null) {
                authHeader = accessor.getFirstNativeHeader("access_token");
            }

            if (authHeader == null) {
                throw new MessagingException("No Authorization header present in STOMP CONNECT");
            }

            String token = authHeader;
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (token == null || token.isBlank()) {
                throw new MessagingException("Empty JWT token");
            }

            String username;
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception ex) {
                throw new MessagingException("Invalid JWT token");
            }

            if (username == null || username.isBlank()) {
                throw new MessagingException("Invalid JWT token: no subject");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new MessagingException("User not found for JWT subject");
            }

            boolean valid = jwtService.validateToken(token, userDetails);
            if (!valid) {
                throw new MessagingException("JWT token is not valid");
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            accessor.setUser(auth);
        } else if (StompCommand.SEND.equals(accessor.getCommand()) ||
                StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            if (accessor.getUser() == null && accessor.getSessionAttributes() != null) {
                Object sessionUser = accessor.getSessionAttributes() != null
                        ? accessor.getSessionAttributes().get("SPRING.AUTH.USER")
                        : null;

                if (sessionUser instanceof UsernamePasswordAuthenticationToken auth) {
                    accessor.setUser(auth);
                }
            }
        }

        if (accessor.getUser() != null && accessor.getSessionAttributes() != null) {
            accessor.getSessionAttributes().put("SPRING.AUTH.USER", accessor.getUser());
        }

        return message;
    }
}

