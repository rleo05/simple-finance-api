package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.Roles;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;

    public void createUser(RegisterRequest request){
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User user = new User(request, encryptedPassword ,Roles.USER);
        userRepository.save(user);

        Account account = new Account();
        account.setBalance(0);
        account.setDocument(request.document());
        account.setFirstName(request.first_name());
        account.setLastName(request.last_name());
        account.setUser(user);
        accountService.createAccount(account);
    }

    public UserDetails findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}