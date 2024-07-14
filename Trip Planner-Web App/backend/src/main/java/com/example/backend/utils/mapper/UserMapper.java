package com.example.backend.utils.mapper;


import com.example.backend.dto.user.UserResponse;
import com.example.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class represents the mapper for the User entity.
 */
@Component
public final class UserMapper {
    public static UserResponse entityToDto(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getPassword(), user.getEmail(),
                user.getGender(), user.getCreatedAt(), user.getDescription());
    }

    public static List<UserResponse> entityListToDto(List<User> users) {
        return users.stream()
                .map(UserMapper::entityToDto)
                .toList();
    }
}
