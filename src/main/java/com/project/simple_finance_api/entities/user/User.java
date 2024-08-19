package com.project.simple_finance_api.entities.user;

import com.project.simple_finance_api.entities.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users_tb")
@Table(name = "users_tb")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToOne(mappedBy = "user")
    private Account account;
}
