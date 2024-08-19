package com.project.simple_finance_api.services;

import com.project.simple_finance_api.dto.auth.RegisterRequest;
import com.project.simple_finance_api.entities.user.Roles;
import com.project.simple_finance_api.entities.user.User;
import com.project.simple_finance_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(RegisterRequest request){
        User user = new User(request, Roles.USER);
        userRepository.save(user);
    }
}
