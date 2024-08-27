package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.util.AccountUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByDocument_ReturnsAccountWithGivenDocument_WhenSuccessful(){
        User savedUser = userRepository.save(new User());
        Account accountToBeSaved = AccountUtil.accountToBeSaved();
        accountToBeSaved.setUser(savedUser);
        accountRepository.save(accountToBeSaved);

        Optional<Account> optionalAccount = accountRepository.findByDocument(accountToBeSaved.getDocument());

        Assertions.assertThat(optionalAccount).isPresent();
        Assertions.assertThat(optionalAccount.get().getDocument())
                .isEqualTo(accountToBeSaved.getDocument());
    }
}