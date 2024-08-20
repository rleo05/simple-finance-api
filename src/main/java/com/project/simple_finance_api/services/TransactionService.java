package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.transaction.DepositRequest;
import com.project.simple_finance_api.dto.transaction.DepositWithdrawalResponse;
import com.project.simple_finance_api.dto.transaction.TransactionResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    public DepositWithdrawalResponse deposit(String document, DepositRequest deposit){
        Account account = accountService.findByDocument(document);
        account.setBalance(account.getBalance() + deposit.amount());
        accountService.createAccount(account);
        Transaction transaction = new Transaction();
        transaction.setAmount(deposit.amount());
        transaction.setTimestamp(getNowInstant());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAccountSender(account);
        transactionRepository.save(transaction);
        return new DepositWithdrawalResponse(transaction);
    }


    private Instant getNowInstant(){
        return LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));
    }
}
