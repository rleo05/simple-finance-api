package com.project.simple_finance_api.dto.transaction;

public record TransferRequest(
        String receiver_document,
        double amount
) {
}
