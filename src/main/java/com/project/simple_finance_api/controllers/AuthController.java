package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.auth.LoginRequest;
import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.repositories.AccountRepository;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request){
        if(userService.findByEmail(request.email()) != null) {
            throw new DataIntegrityViolationException("Error: Email is already registered");
        }
        if(accountRepository.findByDocument(request.document()).isPresent()){
            throw new DataIntegrityViolationException("Error: Document is already registered");
        }

        userService.createUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        try {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token  = tokenService.generateToken((User) auth.getPrincipal());
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(300)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().body("Successfully authenticated!");
        } catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("accessToken", null)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}