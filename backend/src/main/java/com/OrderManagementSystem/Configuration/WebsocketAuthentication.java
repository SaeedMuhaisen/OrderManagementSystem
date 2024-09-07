package com.OrderManagementSystem.Configuration;

import com.OrderManagementSystem.CSR.Repositories.NotificationsRepository;
import com.OrderManagementSystem.CSR.Repositories.TokenRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.CSR.Services.CustomUserDetailsService;
import com.OrderManagementSystem.CSR.Services.JwtService;
import com.OrderManagementSystem.CSR.Services.NotificationServices;
import com.OrderManagementSystem.Entities.Notifications;
import com.OrderManagementSystem.Entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebsocketAuthentication implements ChannelInterceptor  {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final NotificationsRepository notificationsRepository;
    private final NotificationServices notificationServices;

//    @Override
//    public boolean preReceive(MessageChannel channel) {
//        logger.info("What does this do");
//        return true;
//    }
//
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        logger.info("WEBSOCKET - received preSEND: {}", accessor.getCommand());

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            try {
                var auth = authenticateToken(token);
                SecurityContextHolder.getContext().setAuthentication(auth);

                User user = (User) auth.getPrincipal();
                String sessionId = accessor.getSessionId();

                // Check if a notification entry already exists for this user and session
                var existingNotification = notificationsRepository.findByUserAndSessionId(user, sessionId);

                if (existingNotification.isEmpty()) {
                    notificationsRepository.save(
                            Notifications.builder()
                                    .user(user)
                                    .connectedAt(Instant.now())
                                    .sessionId(sessionId)
                                    .build()
                    );
                    logger.info("WEBSOCKET - user subscribed and saved to database: {}", accessor.getCommand());
                } else {
                    logger.info("WEBSOCKET - user already subscribed: {}", accessor.getCommand());
                }
            } catch (Exception e) {
                logger.error("WEBSOCKET - failed to authenticate : error:{}", e.getMessage());
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            notificationServices.deleteBySessionId(accessor.getSessionId());
            logger.info("WEBSOCKET - user disconnected and removed from database: {}", accessor.getSessionId());
        }
        return message;
    }


    public UsernamePasswordAuthenticationToken authenticateToken(String token) throws Exception {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new Exception("Token is null");

        }
        token = token.substring(7);
        var userId = jwtService.extractUserId(token);
        UserDetails userDetails = customUserDetailsService.loadUserById(UUID.fromString(userId));
        var user = userRepository.findById(UUID.fromString(userId));
        boolean isTokenValid = tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (!jwtService.isTokenValid(token, userDetails) || !isTokenValid) {

            throw new Exception("Token is invalid");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities()
        );
        return authToken;
    }
}
