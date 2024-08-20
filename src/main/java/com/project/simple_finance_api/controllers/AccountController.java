package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.account.AccountGetResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.Roles;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.repositories.AccountRepository;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{document}")
    public ResponseEntity<AccountGetResponse> showAccountDetails(@PathVariable String document, HttpServletRequest request) {
        if (!isCorrectToken(request, document)) {
            throw new AccessDeniedException("You don't have access to this account");
        }
        
        return ResponseEntity.ok().body(accountService.getAccountDetails(document));
    }


    private boolean isCorrectToken(HttpServletRequest request, String document){
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("accessToken")) {
                token = cookie.getValue();
            }
        }

        String emailByToken = tokenService.validateToken(token);
        String emailByDocument = accountService.findByDocument(document).getUser().getEmail();
        return emailByDocument.equals(emailByToken);
    }
}