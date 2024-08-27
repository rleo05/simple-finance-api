package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.transaction.DepositWithdrawalRequest;
import com.project.simple_finance_api.dto.transaction.DepositWithdrawalResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.repositories.TransactionRepository;
import com.project.simple_finance_api.util.AccountUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("deposit inserts money to an account and returns DepositWithdrawalResponse")
    @Test
    void deposit_AddMoneyToAccountAndReturnsDepositWithdrawalResponse_WhenSuccessful(){
        Account account = AccountUtil.createValidAccount();
        DepositWithdrawalRequest depositRequest = new DepositWithdrawalRequest(555.5);

        Mockito.when(accountService.findByDocument(account.getDocument())).thenReturn(account);
        Mockito.when(transactionRepository.save(ArgumentMatchers.any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        DepositWithdrawalResponse depositResponse = transactionService.deposit(account.getDocument(), depositRequest);

        Assertions.assertThat(depositResponse).isNotNull();
        Assertions.assertThat(depositResponse.amount()).isEqualTo(depositRequest.amount());
        Assertions.assertThat(account.getBalance()).isEqualTo(555.5);
    }

    @DisplayName("withdrawal retrieves money from an account and returns DepositWithdrawalResponse")
    @Test
    void withdrawal_RetrievesMoneyFromAccountAndReturnsDepositWithdrawalResponse_WhenSuccessful(){
        Account account = AccountUtil.createValidAccount();
        account.setBalance(500);
        DepositWithdrawalRequest withdrawalRequest = new DepositWithdrawalRequest(255.5);

        Mockito.when(accountService.findByDocument(account.getDocument())).thenReturn(account);
        Mockito.when(transactionRepository.save(ArgumentMatchers.any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DepositWithdrawalResponse withdrawalResponse = transactionService.withdrawal(account.getDocument(), withdrawalRequest);

        Assertions.assertThat(withdrawalResponse).isNotNull();
        Assertions.assertThat(withdrawalResponse.amount()).isEqualTo(withdrawalRequest.amount());
        Assertions.assertThat(account.getBalance()).isEqualTo(244.5);
    }
}