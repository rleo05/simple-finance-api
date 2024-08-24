package com.project.simple_finance_api.handler;

import com.project.simple_finance_api.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardExceptionDetails> dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error("DataIntegrityViolation")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardExceptionDetails> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .error("AccessDeniedException")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardExceptionDetails> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("ResourceNotFoundException")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<StandardExceptionDetails> insufficientFundsException(InsufficientFundsException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("InsufficientFundsException")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardExceptionDetails> invalidParamTypeException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("MethodArgumentTypeMismatchException")
                        .message("Invalid param")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardExceptionDetails> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("HttpRequestMethodNotSupportedException")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<StandardExceptionDetails> selfTransferException(SelfTransferException e, HttpServletRequest request) {
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("SelfTransferException")
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionsDetails> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionsDetails
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("MethodArgumentNotValid")
                        .message("Check fields error")
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardExceptionDetails> handlerBadCredentialsException(BadCredentialsException exception, HttpServletRequest request, HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, createInvalidCookie().toString());
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .error("BadCredentials")
                        .message("Invalid username or password")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardExceptionDetails> authenticationException(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, createInvalidCookie().toString());
        return new ResponseEntity<>(
                StandardExceptionDetails
                        .builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .error("BadCredentials")
                        .message("Invalid username or password")
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
                , HttpStatus.UNAUTHORIZED);
    }

    private ResponseCookie createInvalidCookie(){
        return ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
    }
}

