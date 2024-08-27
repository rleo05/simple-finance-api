package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.transaction.*;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.transaction.Transaction;
import com.project.simple_finance_api.entities.transaction.TransactionType;
import com.project.simple_finance_api.exception.InsufficientFundsException;
import com.project.simple_finance_api.repositories.TransactionRepository;
import com.project.simple_finance_api.util.AccountUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceTest {
    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    private final Account account = AccountUtil.createValidAccount();
    private final Account receiver = AccountUtil.createReceiverAccount();

    private final Transaction noTypeTransaction = Transaction.builder()
            .id("ff879563-47a4-4395-816b-5dbb1c6b2570")
            .accountReceiver(account)
            .accountSender(account)
            .build();

    private final Transaction typeTransaction = Transaction.builder()
            .id("gg879563-47a4-4395-816b-5dbb1c6b2570")
            .accountReceiver(account)
            .accountSender(account)
            .type(TransactionType.DEPOSIT)
            .build();

    @BeforeEach
    void setUp(){
        BDDMockito.when(accountService.findByDocument(ArgumentMatchers.anyString()))
                .thenReturn(account);

        BDDMockito.when(transactionRepository.findByAccountSender_IdOrAccountReceiver_IdOrderByTimestampDesc(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(List.of(noTypeTransaction));

        BDDMockito.when(transactionRepository.findByUserIdAndTypeOrderByTimestampDesc(
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(TransactionType.class)))
                .thenReturn(List.of(typeTransaction));
    }

    @DisplayName("deposit inserts money to an account and returns DepositWithdrawalResponse")
    @Test
    void deposit_AddMoneyToAccountAndReturnsDepositWithdrawalResponse_WhenSuccessful(){
        DepositWithdrawalRequest depositRequest = new DepositWithdrawalRequest(555.5);

        DepositWithdrawalResponse depositResponse = transactionService.deposit(account.getDocument(), depositRequest);

        Assertions.assertThat(depositResponse).isNotNull();
        Assertions.assertThat(depositResponse.amount()).isEqualTo(depositRequest.amount());
        Assertions.assertThat(account.getBalance()).isEqualTo(555.5);
    }

    @DisplayName("withdrawal retrieves money from an account and returns DepositWithdrawalResponse")
    @Test
    void withdrawal_RetrievesMoneyFromAccountAndReturnsDepositWithdrawalResponse_WhenSuccessful(){
        account.setBalance(500);
        DepositWithdrawalRequest withdrawalRequest = new DepositWithdrawalRequest(255.5);

        DepositWithdrawalResponse withdrawalResponse = transactionService.withdrawal(account.getDocument(), withdrawalRequest);

        Assertions.assertThat(withdrawalResponse).isNotNull();
        Assertions.assertThat(withdrawalResponse.amount()).isEqualTo(withdrawalRequest.amount());
        Assertions.assertThat(account.getBalance()).isEqualTo(244.5);
    }

    @DisplayName("withdrawal throws InsufficientFundsException when account doesn't have enough money")
    @Test
    void withdrawal_ThrowsInsufficientFundsException_NotEnoughMoney(){
        DepositWithdrawalRequest withdrawalRequest = new DepositWithdrawalRequest(255.5);

        Assertions.assertThatThrownBy(() -> transactionService.withdrawal(account.getDocument(), withdrawalRequest))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("You do not have enough money to complete this withdrawal");
    }

    @DisplayName("transfer sends money from an account to another and returns transfer response")
    @Test
    void transfer_SendsMoneyFromAccountToAnotherAndReturnTransferResponse_WhenSuccessful(){
        BDDMockito.when(accountService.findByDocument("999999999"))
                .thenReturn(receiver);

        account.setBalance(500);

        TransferRequest transferRequest = new TransferRequest(receiver.getDocument(), 250);
        transactionService.transfer(account.getDocument(), transferRequest);

        TransferResponse transferResponse = new TransferResponse(STR."\{account.getFirstName()} \{account.getLastName()}",
                account.getDocument(), STR."\{receiver.getFirstName()} \{receiver.getLastName()}",
                receiver.getDocument(), transferRequest.amount(), LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")));

        Assertions.assertThat(account.getBalance()).isEqualTo(250);
        Assertions.assertThat(receiver.getBalance()).isEqualTo(250);
        Assertions.assertThat(transferResponse).isNotNull();
        Assertions.assertThat(transferResponse.amount()).isEqualTo(250);
    }

    @DisplayName("transfer throws InsufficientFundsException when account doesn't have enough money")
    @Test
    void transfer_ThrowsInsufficientFundsException_NotEnoughMoney(){
        TransferRequest transferRequest = new TransferRequest(receiver.getDocument(), 250);

        Assertions.assertThatThrownBy(() -> transactionService.transfer(account.getDocument(), transferRequest))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("You do not have enough money to complete this withdrawal");
    }

    @DisplayName("historic returns list of transaction response when null type")
    @Test
    void historic_ReturnsListOfTransactionResponse_WhenSuccessful(){
        List<TransactionResponse> historic = transactionService.historic(account.getDocument(), null);

        Assertions.assertThat(historic).isNotNull();
        Assertions.assertThat(historic.getFirst().receiver_document()).isEqualTo(account.getDocument());
    }

    @DisplayName("historic returns list of transaction response with specified type")
    @Test
    void historic_ReturnsListOfTransactionResponseWithType_WhenSuccessful(){
        BDDMockito.when(transactionRepository.findByUserIdAndTypeOrderByTimestampDesc(account.getId(), TransactionType.DEPOSIT))
                .thenReturn(List.of(typeTransaction));

        List<TransactionResponse> historic = transactionService.historic(account.getDocument(), TransactionType.DEPOSIT);

        Assertions.assertThat(historic).isNotNull();
        Assertions.assertThat(historic.getFirst().type()).isEqualTo(TransactionType.DEPOSIT);
        Assertions.assertThat(historic.getFirst().receiver_document()).isEqualTo(account.getDocument());
    }

}