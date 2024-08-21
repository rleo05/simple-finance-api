package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.transaction.*;
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
import java.util.ArrayList;
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
                getNowInstant(), TransactionType.DEPOSIT, account, null);
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
                getNowInstant(), TransactionType.WITHDRAWAL, account, null);
        transactionRepository.save(transaction);
        return new DepositWithdrawalResponse(transaction);
    }

    public List<TransactionResponse> historic(String document, TransactionType type){
        String accountId = accountService.findByDocument(document).getId();

        if(type != null){
            return transactionRepository.findByUserIdAndTypeOrderByTimestampDesc(accountId, type)
                    .stream().map(TransactionResponse::new)
                    .toList();
        }

        return transactionRepository.findByAccountSender_IdOrAccountReceiver_IdOrderByTimestampDesc(accountId, accountId)
                .stream().map(TransactionResponse::new)
                .toList();
    }


    public TransferResponse transfer(String document, TransferRequest transferRequest){
        Account senderAccount = accountService.findByDocument(document);
        double senderBalance = senderAccount.getBalance();

        if(senderBalance < transferRequest.amount()) {
            throw new InsufficientFundsException("You do not have enough money to complete this withdrawal");
        }

        Account receiverAccount = accountService.findByDocument(transferRequest.receiver_document());
        senderAccount.setBalance(senderBalance - transferRequest.amount());
        receiverAccount.setBalance(receiverAccount.getBalance() + transferRequest.amount());
        accountService.createAccount(senderAccount);
        accountService.createAccount(receiverAccount);
        Transaction transaction = new Transaction(transferRequest.amount(),
                getNowInstant(), TransactionType.TRANSFER, senderAccount, receiverAccount);
        transactionRepository.save(transaction);
        return new TransferResponse(STR."\{senderAccount.getFirstName()} \{senderAccount.getLastName()}",
                senderAccount.getDocument(), STR."\{receiverAccount.getFirstName()} \{receiverAccount.getLastName()}",
                receiverAccount.getDocument(), transferRequest.amount(), getNowInstant());

    }

    private Instant getNowInstant(){
        return LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));
    }
}
