package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountSender_Id(String id);
}
