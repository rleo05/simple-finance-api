package com.project.simple_finance_api.controllers;

import com.project.simple_finance_api.dto.transaction.*;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.exception.SelfTransferException;
import com.project.simple_finance_api.services.AccountService;
import com.project.simple_finance_api.services.TokenService;
import com.project.simple_finance_api.services.TransactionService;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
class TransactionControllerTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockHttpServletRequest request;

    private DepositWithdrawalResponse depositWithResponse;
    private User user;
    private Account account;

    @BeforeEach
    void setUp(){
        request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("accessToken", "token");
        request.setCookies(cookie);

        user = new User();
        user.setEmail("test@test.com");
        account = AccountUtil.createValidAccount();
        account.setUser(user);

        BDDMockito.when(accountService.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(account);
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("test@test.com");

        TransferResponse transferResponse = new TransferResponse("test", "9", "test2", "1",
                50, LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")));

        BDDMockito.when(transactionService.transfer(ArgumentMatchers.anyString(), ArgumentMatchers.any(TransferRequest.class)))
                .thenReturn(transferResponse);

        TransactionResponse transactionResponse = new TransactionResponse(account.getDocument(), "12", 50,
                LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")), TransactionType.TRANSFER);

        BDDMockito.when(transactionService.historic(ArgumentMatchers.anyString(), ArgumentMatchers.any(TransactionType.class)))
                .thenReturn(List.of(transactionResponse));
    }

    @Test
    void depositMoney_ReturnsDepositWithdrawalResponse_WhenSuccessful(){
        depositWithResponse = new DepositWithdrawalResponse(account.getDocument(), 50,
                LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")), TransactionType.DEPOSIT);

        BDDMockito.when(transactionService.deposit(ArgumentMatchers.anyString(), ArgumentMatchers.any(DepositWithdrawalRequest.class)))
                .thenReturn(depositWithResponse);

        ResponseEntity<DepositWithdrawalResponse> depositResponse = transactionController
                .depositMoney("d", new DepositWithdrawalRequest(0), request);

        Assertions.assertThat(depositResponse).isNotNull();
        Assertions.assertThat(depositResponse.getBody()).isNotNull();
        Assertions.assertThat(depositResponse.getBody().amount()).isEqualTo(50);
        Assertions.assertThat(depositResponse.getBody().type()).isEqualTo(TransactionType.DEPOSIT);
    }

    @Test
    void depositMoney_ThrowsAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() ->
                        transactionController.depositMoney("d", new DepositWithdrawalRequest(50), request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You cannot make a deposit to this account");

    }

    @Test
    void withdrawalMoney_ReturnsDepositWithdrawalResponse_WhenSuccessful(){
        depositWithResponse = new DepositWithdrawalResponse(account.getDocument(), 50,
                LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")), TransactionType.WITHDRAWAL);

        BDDMockito.when(transactionService.withdrawal(ArgumentMatchers.anyString(), ArgumentMatchers.any(DepositWithdrawalRequest.class)))
                .thenReturn(depositWithResponse);

        ResponseEntity<DepositWithdrawalResponse> withdrawalResponse = transactionController.withdrawalMoney("d", new DepositWithdrawalRequest(0), request);

        Assertions.assertThat(withdrawalResponse).isNotNull();
        Assertions.assertThat(withdrawalResponse.getBody()).isNotNull();
        Assertions.assertThat(withdrawalResponse.getBody().amount()).isEqualTo(50);
        Assertions.assertThat(withdrawalResponse.getBody().type()).isEqualTo(TransactionType.WITHDRAWAL);
    }

    @Test
    void withdrawalMoney_ThrowsAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() ->
                        transactionController.withdrawalMoney("d", new DepositWithdrawalRequest(50), request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You cannot make a withdrawal from this account");
    }

    @Test
    void transferMoney_ReturnsTransferResponse_WhenSuccessful(){
        ResponseEntity<TransferResponse> transferResponse = transactionController.transferMoney("99", new TransferRequest("12", 0), request);

        Assertions.assertThat(transferResponse).isNotNull();
        Assertions.assertThat(transferResponse.getBody()).isNotNull();
        Assertions.assertThat(transferResponse.getBody().sender_document()).isEqualTo("9");

    }

    @Test
    void transferMoney_ThrowsAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() ->
                        transactionController.transferMoney("99", new TransferRequest("12", 0) , request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You cannot transfer money from this account");
    }

    @Test
    void transferMoney_ThrowsSelfTransferException_WhenSameSenderAndReceiverDocument(){
        Assertions.assertThatThrownBy(() ->
                        transactionController.transferMoney("99", new TransferRequest("99", 0) , request))
                .isInstanceOf(SelfTransferException.class)
                .hasMessage("You cannot transfer money to yourself");
    }

    @Test
    void historicTransactions_ReturnsListOfTransactionResponse_WhenSuccessful(){
        ResponseEntity<List<TransactionResponse>> listTransactionResponse = transactionController
                .historicTransactions("d", request, TransactionType.TRANSFER);

        Assertions.assertThat(listTransactionResponse).isNotNull();
        Assertions.assertThat(listTransactionResponse.getBody().getFirst()).isNotNull();
        Assertions.assertThat(listTransactionResponse.getBody().getFirst().sender_document()).isEqualTo("111111111");
    }

    @Test
    void historicTransactions_ThrowsAccessDeniedException_WhenInvalidToken(){
        BDDMockito.when(tokenService.validateToken(ArgumentMatchers.anyString()))
                .thenReturn("invalidEmail@test.com");

        Assertions.assertThatThrownBy(() ->
                        transactionController.historicTransactions("d", request, TransactionType.TRANSFER))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("You cannot see the transaction historic from this account");
    }
}