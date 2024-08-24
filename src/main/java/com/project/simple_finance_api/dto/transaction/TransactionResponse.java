package com.project.simple_finance_api.dto.transaction;

import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;

import java.time.Instant;

public record TransactionResponse(
        String sender_document,
        String receiver_document,
        double amount,
        Instant timestamp,
        TransactionType type
) {
    public TransactionResponse(Transaction transaction){
        this(transaction.getAccountSender() != null ? transaction.getAccountSender().getDocument() : null,
                transaction.getAccountReceiver() != null ? transaction.getAccountReceiver().getDocument() : null,
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getType());
    }
}