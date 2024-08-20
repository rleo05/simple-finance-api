package com.project.simple_finance_api.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.simple_finance_api.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "accounts_tb")
@Table(name = "accounts_tb")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;

    private String firstName;
    private String lastName;
    private String document;
    private double balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
