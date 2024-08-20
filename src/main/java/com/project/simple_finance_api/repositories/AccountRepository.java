package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByDocument(String document);
}
