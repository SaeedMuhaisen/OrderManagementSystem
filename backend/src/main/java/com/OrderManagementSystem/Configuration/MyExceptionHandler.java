package com.OrderManagementSystem.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class MyExceptionHandler extends DefaultHandlerExceptionResolver {
    private final static Logger logger= LoggerFactory.getLogger(MyExceptionHandler.class);
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handle(HttpMessageNotReadableException e, HttpServletRequest request) {

        logger.error("User sent a request with malformed body to endpoint: {} - received:{} - errorMessage: {}", request.getRequestURI(),request.getHeader("body"), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handle(RuntimeException e,HttpServletRequest request) {
        logger.error("CRITICAL ERROR OCCURRED - RUNTIME EXCEPTION CAUGHT: {} - ENDPOINT: {}",e.getCause(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("Access denied", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }
}

