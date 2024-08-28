package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.repositories.AccountRepository;
import com.project.simple_finance_api.repositories.UserRepository;
import com.project.simple_finance_api.services.UserService;
import com.project.simple_finance_api.util.AccountUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
class AuthControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp(){
        User user = new User();
        BDDMockito.when(userService.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(user);
        Account account = AccountUtil.createValidAccount();
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.ofNullable(account));
    }

    @Test
    void register_ReturnsCreated_WhenSuccess(){
        RegisterRequest request = new RegisterRequest("test@test.com", "testpassword",
                "test", "test", "999999999");

        Assertions.assertThatCode(() -> userService.createUser(request))
                .doesNotThrowAnyException();

    }

    @Test
    void register_ThrowsDataIntegrityViolationException_WhenEmailAlreadyRegistered(){
        RegisterRequest request = new RegisterRequest("test@test.com", "testpassword",
                "test", "test", "999999999");
        User user = new User();
        user.setEmail("test@test.com");
        BDDMockito.when(userService.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(user);

        Assertions.assertThatThrownBy(() -> authController.register(request))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Error: Email is already registered");
    }

    @Test
    void register_ThrowsDataIntegrityViolationException_WhenDocumentAlreadyRegistered(){
        BDDMockito.when(userService.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(null);

        RegisterRequest request = new RegisterRequest("test@test.com", "testpassword",
                "test", "test", "999999999");

        Account account = new Account();
        account.setDocument("999999999");
        BDDMockito.when(accountRepository.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() -> authController.register(request))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Error: Document is already registered");
    }
}