package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.Roles;
import com.project.simple_finance_api.entities.user.User;
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
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserService userService;

    private final Account account = AccountUtil.createValidAccount();
    private RegisterRequest request;
    private User user;

    @BeforeEach
    void setUp(){
        request = new RegisterRequest("test@test.com", "test", "name",
                "lastname", "99999999");
        user = new User();

        BDDMockito.doNothing().when(userRepository).delete(ArgumentMatchers.any(User.class));
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(user);
    }

    @Test
    void createUser_CreateANewAccountAndUser_WhenSuccessful(){
        userService.createUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        Mockito.verify(accountService).createAccount(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        Assertions.assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedUser.getRole()).isEqualTo(Roles.USER);
        Assertions.assertThat(capturedAccount.getFirstName()).isEqualTo(request.first_name());
        Assertions.assertThat(capturedAccount.getLastName()).isEqualTo(request.last_name());
        Assertions.assertThat(capturedAccount.getBalance()).isEqualTo(0);
    }

    @Test
    void deleteUser_DeleteAValidUser_WhenSuccessful(){
        Assertions.assertThatCode(() -> userService.deleteUser(user))
                .doesNotThrowAnyException();
    }

    @Test
    void findByEmail_ReturnsUserDetailsByEmail_WhenSuccessful(){
        UserDetails userDetails = userService.findByEmail("test@test.com");

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
    }
}