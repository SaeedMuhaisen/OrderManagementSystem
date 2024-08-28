package com.OrderManagementSystem.Configuration;

import com.OrderManagementSystem.Entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.OrderManagementSystem.Entities.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class MySecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.addAllowedOrigin("http://localhost:8081"); // Add your frontend's origin        configuration.addAllowedOrigin("http://localhost:8081"); // Add your frontend's origin
        configuration.addAllowedOriginPattern("**"); // Add your frontend's origin

        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/public/**",
                                "/api/register/**",
                                "/api/register",
                                "api/preview",
                                "api/preview/**"
                        )
                        .permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole(ADMIN.name())
                        .requestMatchers("/api/seller/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/buyer/**").hasAnyRole(Role.BUYER.name(), ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
