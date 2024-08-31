package com.OrderManagementSystem.CSR.Services;

import com.OrderManagementSystem.CSR.Repositories.TokenRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.enums.Role;
import com.OrderManagementSystem.Entities.Token;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.TokenType;
import com.OrderManagementSystem.Entities.enums.UserStatus;
import com.OrderManagementSystem.Exceptions.AuthExceptions.CouldNotAuthException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserAlreadyExistsException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserBannedException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Models.Authentication.AuthenticationRequest;
import com.OrderManagementSystem.Models.Authentication.AuthenticationResponse;

import com.OrderManagementSystem.Models.Authentication.RegisterRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@RequiredArgsConstructor
@Component
public class AuthenticationServices {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {


        if (authenticationRequest.getEmail() == null || authenticationRequest.getEmail().isBlank() ||
                authenticationRequest.getPassword()==null || authenticationRequest.getPassword().isBlank()) {
            throw new CouldNotAuthException("Something wrong with user Credentials, registerRequest contains invalid items");
        }



        var userExists = userRepository.findByEmail(authenticationRequest.getEmail());
        if (!userExists.isPresent()) {
            throw new UserNotFoundException("User doesn't exist in Database");
        }

        User user = userExists.get();
        if(user.getUserStatus().equals(UserStatus.BANNED)){
            throw new UserBannedException("User with userid: "+user.getId()+" is banned");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(user.getRole().name())
                .build();
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        if (registerRequest.getEmail() == null || registerRequest.getEmail().isBlank() ||
                registerRequest.getPassword()==null || registerRequest.getPassword().isBlank()) {
            throw new CouldNotAuthException("Something wrong with user Credentials, registerRequest contains invalid items");
        }
        var userExists = userRepository.findByEmail(registerRequest.getEmail());
        if (userExists.isPresent()) {
            if(userExists.get().getUserStatus().equals(UserStatus.BANNED)){
                throw new UserBannedException("User is banned, id: "+ userExists.get().getId());
            }
            else{
                throw new UserAlreadyExistsException("Email address is already in use: " + userExists.get().getEmail());
            }
        }
        var set=new HashSet<Role>();
        set.add(Role.SELLER);
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userStatus(UserStatus.ACTIVE)
                .role(Role.SELLER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .build();
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    public AuthenticationResponse refreshToken(String refreshToken) throws Exception {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new Exception("Refresh token is not valid | refresh token is null or empty");
        }
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            var user=userRepository.getReferenceById(((User)userDetails).getId());
            saveUserToken(user,newAccessToken);

            return AuthenticationResponse.builder()
                    .refreshToken(newRefreshToken)
                    .accessToken(newAccessToken)
                    .build();

        } else {
           throw new Exception("failed to generate new tokens");
        }
    }
}
