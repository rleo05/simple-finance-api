package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.account.AccountGetResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.exception.InsufficientFundsException;
import com.project.simple_finance_api.exception.ResourceNotFoundException;
import com.project.simple_finance_api.repositories.AccountRepository;
import com.project.simple_finance_api.repositories.UserRepository;
import com.project.simple_finance_api.util.AccountUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    private final Account account = AccountUtil.createValidAccount();

    @BeforeEach
    void setUp(){
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.ofNullable(account));
    }

    @Test
    void findByDocument_ReturnAnAccountUsingGivenDocument_WhenSuccessful(){
        Account accountByDocument = accountService.findByDocument("999999999");

        Assertions.assertThat(accountByDocument).isNotNull();
        Assertions.assertThat(accountByDocument.getDocument()).isEqualTo(account.getDocument());
        Assertions.assertThat(accountByDocument.getFirstName()).isEqualTo(account.getFirstName());
        Assertions.assertThat(accountByDocument.getBalance()).isEqualTo(0);
    }

    @Test
    void findByDocument_ThrowsResourceNotFoundException_WhenAccountNotFound(){
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.findByDocument("999999999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invalid document");
    }

    @Test
    void createAccount_CreateAValidAccount_WhenSuccessful(){
        accountService.createAccount(account);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountRepository).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        Assertions.assertThat(capturedAccount).isNotNull();
        Assertions.assertThat(capturedAccount.getDocument()).isEqualTo(account.getDocument());
        Assertions.assertThat(capturedAccount.getFirstName()).isEqualTo(account.getFirstName());
    }

    @Test
    void getAccountDetails_ReturnsAccountGetResponse_WhenSuccessful(){
        AccountGetResponse accountDetails = accountService.getAccountDetails("999999999");

        Assertions.assertThat(accountDetails).isNotNull();
        Assertions.assertThat(accountDetails.document()).isEqualTo(account.getDocument());
        Assertions.assertThat(accountDetails.name()).isEqualTo(STR."\{account.getFirstName()} \{account.getLastName()}");
    }

    @Test
    void getAccountDetails_ThrowsResourceNotFoundException_WhenAccountNotFound(){
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.getAccountDetails("99999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invalid document");
    }

    @Test
    void deleteAccount_DeleteAnValidAccount_WhenSuccessful(){
        Assertions.assertThatCode(() -> accountService.deleteAccount("99999"))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteAccount_ThrowsResourceNotFoundException_WhenAccountNotFound(){
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.deleteAccount("9"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Invalid document");
    }
}