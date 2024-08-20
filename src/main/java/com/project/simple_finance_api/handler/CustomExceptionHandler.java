package com.project.simple_finance_api.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.simple_finance_api.exception.StandardExceptionDetails;
import com.project.simple_finance_api.exception.ValidationExceptionsDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<StandardExceptionDetails> tokenExpiredException(TokenExpiredException e, HttpServletRequest request){
//        return new ResponseEntity<>(
//                StandardExceptionDetails
//                        .builder()
//                        .status(HttpStatus.UNAUTHORIZED.value())
//                        .error("TokenExpired")
//                        .message("Token already expired, sign in again.")
//                        .timestamp(LocalDateTime.now())
//                        .path(request.getRequestURI())
//                        .build()
//                , HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardExceptionDetails> handlerBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
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


}
