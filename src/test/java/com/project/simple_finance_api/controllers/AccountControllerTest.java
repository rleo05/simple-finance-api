package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.account.AccountGetResponse;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.util.AccountUtil;
import jakarta.servlet.http.Cookie;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
class AccountControllerTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp(){
        request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("accessToken", "teste");
        request.setCookies(cookie);

        AccountGetResponse accountGetResponse = new AccountGetResponse("test", "999999999", 0);
        User user = new User();
        user.setEmail("test@test.com");
        Account account = AccountUtil.createValidAccount();
        account.setUser(user);

        BDDMockito.when(accountService.getAccountDetails(ArgumentMatchers.anyString()))
                .thenReturn(accountGetResponse);
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("test@test.com");
        BDDMockito.when(accountService.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(account);
        BDDMockito.doNothing().when(accountService)
                .deleteAccount(ArgumentMatchers.anyString());
    }

    @Test
    void showAccountDetails_ReturnsDetailsAboutAnAccount_WhenSuccessful(){
        ResponseEntity<AccountGetResponse> accountGetResponse = accountController.showAccountDetails("test", request);

        Assertions.assertThat(accountGetResponse.getBody()).isNotNull();
        Assertions.assertThat(accountGetResponse.getBody().name()).isEqualTo("test");
    }

    @Test
    void showAccountDetails_ThrowAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() -> accountController.showAccountDetails("d", request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You don't have access to this account");
    }

    @Test
    void deleteAccount_DeletesAccountWithGivenDocument_WhenSuccessful(){
        Assertions.assertThatCode(() -> accountController.deleteAccount("d", request))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteAccount_ThrowAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() -> accountController.deleteAccount("d", request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You cannot delete this account");
    }
}