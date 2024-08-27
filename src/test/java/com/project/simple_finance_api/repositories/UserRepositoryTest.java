package com.project.simple_finance_api.repositories;

import com.project.simple_finance_api.entities.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findByEmail_ReturnsUserWithGivenEmail_WhenSuccessful(){
        User userToBeSaved = new User();
        userToBeSaved.setEmail("test@test.com");
        userRepository.save(userToBeSaved);

        UserDetails userByEmail = userRepository.findByEmail(userToBeSaved.getEmail());

        Assertions.assertThat(userByEmail).isNotNull();
        Assertions.assertThat(userByEmail.getUsername()).isEqualTo(userToBeSaved.getEmail());
    }
}