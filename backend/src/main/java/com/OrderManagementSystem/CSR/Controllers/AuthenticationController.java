package com.OrderManagementSystem.CSR.Controllers;
import com.OrderManagementSystem.CSR.Services.AuthenticationServices;
import com.OrderManagementSystem.CSR.Services.JwtService;
import com.OrderManagementSystem.Exceptions.AuthExceptions.CouldNotAuthException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserAlreadyExistsException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserBannedException;
import com.OrderManagementSystem.Exceptions.AuthExceptions.UserNotFoundException;
import com.OrderManagementSystem.Models.Authentication.AuthenticationRequest;
import com.OrderManagementSystem.Models.Authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServices authenticationServices;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    /**
     * On Signup:
     * 200 ok
     * 409 Conflict - which means Users Already exist
     * 403 Forbidden - user exists and is banned
     * 500 Something went wrong with authenticating, either from oauth server or code
     *
     * on Login:
     * 200 ok
     * 404 Not_found: user doesn't exist in database
     * 403 Forbidden: user is banned
     * 500 Internal server error - something went wrong in authentication either from oauth server or code
     *
     * */


    @PostMapping("/Login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("login request received user: ${}", authenticationRequest);
        try {
            var response = authenticationServices.login(authenticationRequest);
            logger.info("User Login successful - user id: {}", response.getUserId());
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            logger.warn("User Login failed: UserNotFoundException {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserBannedException e) {
            logger.warn("User Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CouldNotAuthException e) {
            logger.warn("User Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("User Login failed: something unexpected happened: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/Signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("register - register request received: {}", registerRequest);
        try {
            var response = authenticationServices.register(
                    registerRequest);
            logger.info("register - success for :{}", registerRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            logger.warn("register - failed - email already in use: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UserBannedException e) {
            logger.warn("register - failed - User exists but banned: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CouldNotAuthException e) {
            logger.warn("register - failed - Couldn't authenticate: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("register - failed - something unexpected happened: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        logger.info("Token refresh request received for refresh token: {}", refreshToken);
        try {
            var result=authenticationServices.refreshToken(refreshToken);
            logger.info("Token Refreshed successfully: {}", result);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.error("Token refresh failed: Internal server error for refresh token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
