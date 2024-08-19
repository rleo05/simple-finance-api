package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.auth.LoginRequest;
import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest request){
        if(userService.findByEmail(request.email()) != null) {
            throw new DataIntegrityViolationException("Error: Email is already registered");
        }
        userService.createUser(request);
    }

    @PostMapping("/login")
    public String register(@RequestBody LoginRequest request){
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        return "authenticated";
    }

}