package com.example.backend.repository;

import com.example.backend.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepoTest {
    @MockBean
    private UserRepo userRepo;

    @Test
    void findByUsername() {
        // Setup
        String username = "Test Username";
        User expectedUser = new User();
        expectedUser.setUsername(username);

        Mockito.when(userRepo.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Execution
        Optional<User> result = userRepo.findByUsername(username);

        // Verify
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    void existsByUsernameOrEmail() {
        // Setup
        String username = "Test Username";
        String email = "Test Email";

        Mockito.when(userRepo.existsByUsernameOrEmail(username, email)).thenReturn(true);

        // Execution
        boolean result = userRepo.existsByUsernameOrEmail(username, email);

        // Verify
        assertThat(result).isTrue();
    }
}