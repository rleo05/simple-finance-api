package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.util.AccountUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    private Account account;

    @BeforeEach
    void setUp(){
        User savedUser = userRepository.save(new User());
        Account accountToBeSaved = AccountUtil.accountToBeSaved();
        accountToBeSaved.setUser(savedUser);
        account = accountRepository.save(accountToBeSaved);

        Transaction transactionToBeSaved = Transaction.builder()
                .accountSender(account)
                .type(TransactionType.DEPOSIT)
                .build();
        transactionRepository.save(transactionToBeSaved);
    }

    @Test
    void findByAccountSender_ReturnsListOfTransactionWithGivenId_WhenSuccessful(){
        List<Transaction> findByAccountSender = transactionRepository
                .findByAccountSender_IdOrAccountReceiver_IdOrderByTimestampDesc(account.getId(), account.getId());

        Assertions.assertThat(findByAccountSender).isNotEmpty();
        Assertions.assertThat(findByAccountSender.getFirst().getAccountSender().getDocument())
                .isEqualTo(account.getDocument());
    }

    @Test
    void findByAccountSenderAndType_ReturnsListOfTransactionWithGivenIdAndType_WhenSuccessful(){
        List<Transaction> findByAccountSenderAndType = transactionRepository
                .findByUserIdAndTypeOrderByTimestampDesc(account.getId(), TransactionType.DEPOSIT);

        Assertions.assertThat(findByAccountSenderAndType).isNotEmpty();
        Assertions.assertThat(findByAccountSenderAndType.getFirst().getAccountSender().getDocument())
                .isEqualTo(account.getDocument());
        Assertions.assertThat(findByAccountSenderAndType.getFirst().getType())
                .isEqualTo(TransactionType.DEPOSIT);
    }
}