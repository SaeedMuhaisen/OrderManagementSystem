package com.OrderManagementSystem.Configuration;

import com.OrderManagementSystem.CSR.Repositories.NotificationsRepository;
import com.OrderManagementSystem.CSR.Repositories.TokenRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.CSR.Services.CustomUserDetailsService;
import com.OrderManagementSystem.CSR.Services.JwtService;
import com.OrderManagementSystem.CSR.Services.NotificationServices;
import com.OrderManagementSystem.Entities.Notifications;
import com.OrderManagementSystem.Entities.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebsocketAuthentication websocketAuthentication;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final NotificationsRepository notificationsRepository;
    //private final NotificationServices notificationServices;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("http://localhost:50576","http://192.168.8.181:50576") // Replace with your frontend origin
                //.setAllowedOrigins("http://192.168.8.181:50576") // Replace with your frontend origin
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketAuthentication);
    }


}
