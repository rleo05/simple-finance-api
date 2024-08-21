package com.project.simple_finance_api.dto.transaction;

import java.time.Instant;

public record TransferResponse(
        String sender_name,
        String sender_document,
        String receiver_name,
        String receiver_document,
        double amount,
        Instant timestamp) {

}
