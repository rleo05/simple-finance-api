package com.project.simple_finance_api.dto.transaction;

import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;

import java.time.Instant;

public record DepositWithdrawalResponse(
        String sender_document,
        double amount,
        Instant timestamp,
        TransactionType type
) {

    public DepositWithdrawalResponse(Transaction transaction){
        this(transaction.getAccountSender().getDocument(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getType());
    }
}
