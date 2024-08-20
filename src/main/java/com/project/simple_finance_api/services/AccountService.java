package com.project.simple_finance_api.services;

import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account findByDocument(String document){
        return accountRepository.findByDocument(document);
    }

    public void createAccount(Account account){
        accountRepository.save(account);
    }
}
