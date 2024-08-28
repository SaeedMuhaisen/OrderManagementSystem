package com.OrderManagementSystem.Configuration;

import com.OrderManagementSystem.CSR.Repositories.TokenRepository;
import com.OrderManagementSystem.CSR.Services.CustomUserDetailsService;
import com.OrderManagementSystem.CSR.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final CustomUserDetailsService customUserDetailsService;

  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/register")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
      return;
    }

    final String jwt = authHeader.substring(7);
    var userId = jwtService.extractUserId(jwt);

    if (userId == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
      return;
    }
    UserDetails userDetails = this.customUserDetailsService.loadUserById(UUID.fromString(userId));
    boolean isTokenValid = tokenRepository.findByToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);

    if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              jwt,
              userDetails.getAuthorities()
      );
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    } else {
      if (!jwtService.isTokenValid(jwt, userDetails)) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
      }
      return;
    }

    filterChain.doFilter(request, response);
  }
}