package com.example.backend.service;

import com.example.backend.dto.user.UserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.dto.user.UserUpdateRequest;
import com.example.backend.exception.AuthException;
import com.example.backend.model.User;
import com.example.backend.model.enums.Gender;
import com.example.backend.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    void saveShouldPersistUser() {
        UserRequest userRequest = new UserRequest("username", "name", "password", "email", Gender.MALE);
        User user = new User(userRequest.username(), userRequest.name(), userRequest.password(),
                userRequest.email(), userRequest.gender(), LocalDateTime.now(), "default description");
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.save(userRequest);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.username());
    }

    @Test
    void updateShouldChangeUserDetails() {
        String username = "existingUser";
        User existingUser = new User(username, "oldName", "oldPassword", "oldEmail", Gender.MALE, LocalDateTime.now(), "oldDescription");
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(existingUser));

        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", "newEmail", Gender.FEMALE, "newDescription");
        when(userRepo.save(any(User.class))).thenReturn(existingUser);

        UserResponse result = userService.update(updateRequest, username);

        assertEquals(updateRequest.name(), result.name());
        assertEquals(updateRequest.email(), result.email());
        assertEquals(updateRequest.gender(), result.gender());
    }

    @Test
    void deleteShouldRemoveUser() {
        Long userId = 1L;
        User user = new User("username", "name", "password", "email", Gender.MALE, LocalDateTime.now(), "description");
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepo).delete(user);

        assertDoesNotThrow(() -> userService.delete(userId));
        verify(userRepo).delete(user);
    }

    @Test
    void findByIdShouldThrowNotFoundException() {
        Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AuthException.NotFoundException.class, () -> userService.findById(userId));
        assertTrue(exception.getMessage().contains("User not found with id: " + userId));
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(
                new User("user1", "name1", "pass1", "email1", Gender.MALE, LocalDateTime.now(), "desc1"),
                new User("user2", "name2", "pass2", "email2", Gender.FEMALE, LocalDateTime.now(), "desc2"));
        when(userRepo.findAll()).thenReturn(users);

        List<UserResponse> results = userService.getAllUserResponses();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(users.get(0).getUsername(), results.get(0).username());
    }
}
