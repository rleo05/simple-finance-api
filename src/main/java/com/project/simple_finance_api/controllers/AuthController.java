package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.auth.LoginRequest;
import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request){
        userService.createUser(request);
    }

    @PostMapping("/login")
    public void register(@RequestBody LoginRequest request){
    }

}
