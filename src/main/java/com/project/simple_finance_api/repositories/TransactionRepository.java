package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByAccountSender_IdOrAccountReceiver_IdOrderByTimestampDesc(String senderId, String receiverId);
    @Query("SELECT t FROM transactions_tb t WHERE (t.accountSender.id = :userId OR t.accountReceiver.id = :userId) AND t.type = :type ORDER BY t.timestamp DESC")
    List<Transaction> findByUserIdAndTypeOrderByTimestampDesc(@Param("userId") String userId, @Param("type") TransactionType type);
}
