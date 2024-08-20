package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByDocument(String document);
}
