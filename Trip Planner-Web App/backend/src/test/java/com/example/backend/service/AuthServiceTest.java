package com.example.backend.service;

import com.example.backend.dto.user.UserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.exception.AuthException;
import com.example.backend.model.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void signupSuccess() throws AuthException {
        // Arrange
        UserRequest userRequest = new UserRequest("username", "name", "password",
                "email@example.com", Gender.FEMALE);
        UserResponse userResponse = new UserResponse(1L, "username", "name", "password",
                "email@example.com", Gender.FEMALE, LocalDateTime.now(), "description");
        when(userService.checkIfUsernameOrEmailExists(userRequest.username(), userRequest.email())).thenReturn(false);
        when(userService.save(userRequest)).thenReturn(userResponse);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        UserResponse result = authService.signup(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("username", result.username());
        verify(inMemoryUserDetailsManager).createUser(any(UserDetails.class));
    }

    @Test
    void signupFailsWhenUserExists() {
        // Arrange
        UserRequest userRequest = new UserRequest("username", "name", "password",
                "email@example.com", Gender.FEMALE);
        when(userService.checkIfUsernameOrEmailExists(userRequest.username(), userRequest.email())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthException.class, () -> authService.signup(userRequest));
    }
}
