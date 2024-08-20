package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.transaction.DepositWithdrawalRequest;
import com.project.simple_finance_api.dto.transaction.DepositWithdrawalResponse;
import com.project.simple_finance_api.dto.transaction.TransactionResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.exception.InsufficientFundsException;
import com.project.simple_finance_api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    public DepositWithdrawalResponse deposit(String document, DepositWithdrawalRequest deposit){
        Account account = accountService.findByDocument(document);
        account.setBalance(account.getBalance() + deposit.amount());
        accountService.createAccount(account);
        Transaction transaction = new Transaction(deposit.amount(),
                getNowInstant(), TransactionType.DEPOSIT, account);
        transactionRepository.save(transaction);
        return new DepositWithdrawalResponse(transaction);
    }

    public DepositWithdrawalResponse withdrawal(String document, DepositWithdrawalRequest deposit){
        Account account = accountService.findByDocument(document);
        double accountBalance = account.getBalance();
        if(accountBalance < deposit.amount()) {
            throw new InsufficientFundsException("You do not have enough money to complete this withdrawal");
        }
        account.setBalance(accountBalance - deposit.amount());
        accountService.createAccount(account);
        Transaction transaction = new Transaction(deposit.amount(),
                getNowInstant(), TransactionType.WITHDRAWAL, account);
        transactionRepository.save(transaction);
        return new DepositWithdrawalResponse(transaction);
    }

    public List<TransactionResponse> historic(String document){
        String accountId = accountService.findByDocument(document).getId();
        return transactionRepository.findByAccountSender_Id(accountId)
                .stream().map(TransactionResponse::new)
                .toList();
    }


    private Instant getNowInstant(){
        return LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));
    }
}
