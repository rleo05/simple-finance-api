package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.transaction.DepositWithdrawalRequest;
import com.project.simple_finance_api.dto.transaction.DepositWithdrawalResponse;
import com.project.simple_finance_api.dto.transaction.TransactionResponse;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.services.TransactionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/{document}/deposit")
    public ResponseEntity<DepositWithdrawalResponse> depositMoney(@PathVariable String document, @RequestBody @Valid DepositWithdrawalRequest deposit, HttpServletRequest request) {
        if (isCorrectToken(request, document)) {
            throw new AccessDeniedException("You cannot make a deposit to this account");
        }
        return ResponseEntity.ok().body(transactionService.deposit(document, deposit));
    }

    @PostMapping(path = "/{document}/withdrawal")
    public ResponseEntity<DepositWithdrawalResponse> withdrawalMoney(@PathVariable String document, @RequestBody @Valid DepositWithdrawalRequest deposit, HttpServletRequest request) {
        if (isCorrectToken(request, document)) {
            throw new AccessDeniedException("You cannot make a withdrawal from this account");
        }
        return ResponseEntity.ok().body(transactionService.withdrawal(document, deposit));
    }

    @GetMapping(path = "/{document}/historic")
    public ResponseEntity<List<TransactionResponse>> historicTransactions(@PathVariable String document, HttpServletRequest request, @RequestParam(required = false) TransactionType type){
        if (isCorrectToken(request, document)) {
            throw new AccessDeniedException("You cannot see the transaction historic from this account");
        }
        return ResponseEntity.ok().body(transactionService.historic(document, type));
    }

    private boolean isCorrectToken(HttpServletRequest request, String document){
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("accessToken")) {
                token = cookie.getValue();
            }
        }

        String emailByDocument = accountService.findByDocument(document).getUser().getEmail();
        String emailByToken = tokenService.validateToken(token);
        return !emailByDocument.equals(emailByToken);
    }
}
