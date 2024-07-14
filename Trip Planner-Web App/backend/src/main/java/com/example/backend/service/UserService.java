package com.example.backend.service;

import com.example.backend.dto.user.UserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.dto.user.UserUpdateRequest;
import com.example.backend.exception.AuthException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;
import com.example.backend.utils.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents the service for the User entity.
 */
@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public UserResponse save(UserRequest userRequest) {
        User userToSave = new User(userRequest.username(), userRequest.name(), userRequest.password(),
                userRequest.email(), userRequest.gender(),
                LocalDateTime.now(), "You can add a description here.");
        return UserMapper.entityToDto(userRepo.save(userToSave));
    }

    @Transactional
    public UserResponse update(UserUpdateRequest userRequest, String username) {
        User userToUpdate = findByUsername(username);

        userToUpdate.setName(userRequest.name());
        userToUpdate.setEmail(userRequest.email());
        userToUpdate.setGender(userRequest.gender());
        userToUpdate.setDescription(userRequest.description());

        return UserMapper.entityToDto(userRepo.save(userToUpdate));
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepo.delete(user);
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new AuthException.NotFoundException("User not found with id: " + id));
    }

    public UserResponse findResponseByUsername(String username) {
        return UserMapper.entityToDto(findByUsername(username));
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new AuthException.NotFoundException("User not found with username: " + username));
    }

    public List<UserResponse> getAllUserResponses() {
        List<User> users = userRepo.findAll();
        return UserMapper.entityListToDto(users);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public boolean checkIfUsernameOrEmailExists(String username, String email) {
        return userRepo.existsByUsernameOrEmail(username, email);
    }
}
