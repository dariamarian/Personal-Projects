package com.example.backend.dto.user;

import com.example.backend.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * This class represents the user response.
 */
public record UserResponse(
        @Schema(description = "The id of the user")
        Long id,
        @Schema(description = "The username of the user")
        String username,
        @Schema(description = "The name of the user")
        String name,
        @Schema(description = "The password of the user")
        String password,
        @Schema(description = "The email of the user")
        String email,
        @Schema(description = "The gender of the user")
        Gender gender,
        @Schema(description = "The date and time when the user was created")
        LocalDateTime createdAt,
        @Schema(description = "The description of the user")
        String description
) {
    public UserResponse(Long id, String username, String name, String password, String email, Gender gender, LocalDateTime createdAt, String description) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.createdAt = createdAt;
        this.description = description;
    }
}
