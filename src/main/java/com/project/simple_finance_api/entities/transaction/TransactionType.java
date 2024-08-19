package com.project.simple_finance_api.entities.transaction;

public enum TransactionType {
    WITHDRAWAL("WITHDRAWAL"),
    DEPOSIT("DEPOSIT"),
    TRANSFER("TRANSFER");

    final String code;
    TransactionType(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
