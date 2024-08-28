package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.account.AccountGetResponse;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{document}")
    public ResponseEntity<AccountGetResponse> showAccountDetails(@PathVariable String document, HttpServletRequest request) {
        if (isCorrectToken(request, document)) {
            throw new AccessDeniedException("You don't have access to this account");
        }
        
        return ResponseEntity.ok().body(accountService.getAccountDetails(document));
    }

    @DeleteMapping(path = "/{document}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String document, HttpServletRequest request){
        if (isCorrectToken(request, document)) {
            throw new AccessDeniedException("You cannot delete this account");
        }

        accountService.deleteAccount(document);
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

        return !emailByDocument.equals(emailByToken);
    }
}