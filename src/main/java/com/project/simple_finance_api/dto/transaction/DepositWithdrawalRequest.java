package com.project.simple_finance_api.dto.transaction;

import jakarta.validation.constraints.DecimalMin;

public record DepositWithdrawalRequest(
        @DecimalMin(value = "0.0", inclusive = false, message = "The minimum value to complete a transaction is 0.1")
        double amount
) {
}
