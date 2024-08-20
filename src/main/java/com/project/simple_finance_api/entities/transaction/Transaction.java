package com.project.simple_finance_api.entities.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.simple_finance_api.entities.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity(name = "transactions_tb")
@Table(name = "transactions_tb")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    String id;

    double amount;
    Instant timestamp;
    @Enumerated(EnumType.STRING)
    TransactionType type;

    @ManyToOne
    @JoinColumn(name="sender")
    Account accountSender;

    @ManyToOne
    @JoinColumn(name="receiver")
    Account accountReceiver;
}
