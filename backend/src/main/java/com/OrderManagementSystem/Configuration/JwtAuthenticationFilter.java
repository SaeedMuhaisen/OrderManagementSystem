package com.OrderManagementSystem.Configuration;

import com.OrderManagementSystem.CSR.Controllers.SellerController;
import com.OrderManagementSystem.CSR.Repositories.TokenRepository;
import com.OrderManagementSystem.CSR.Services.CustomUserDetailsService;
import com.OrderManagementSystem.CSR.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final CustomUserDetailsService customUserDetailsService;

  private final TokenRepository tokenRepository;
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    if (request.getServletPath().contains("/api/register")) {
      logger.info("Skipping authentication for /api/register");
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.warn("Missing or invalid Authorization header");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    final String jwt = authHeader.substring(7);
    var userId = jwtService.extractUserId(jwt);
    if (userId == null) {
      logger.warn("Could not extract user ID from JWT");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    try {
      UserDetails userDetails = this.customUserDetailsService.loadUserById(UUID.fromString(userId));
      boolean isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked())
              .orElse(false);

      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, jwt, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("Authentication successful for user: "+ userId);
      } else {
        if (!jwtService.isTokenValid(jwt, userDetails)) {
          logger.warn("Invalid JWT token");
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
          logger.warn("Token is not valid in repository");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return;
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      logger.error("Error in filter chain", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    logger.info("Response Status: {}", response.getStatus());
  }


}