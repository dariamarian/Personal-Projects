package com.example.backend.dto.user;

import com.example.backend.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Schema(description = "The name of the user (required)")
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters")
        String name,
        @Schema(description = "The email of the user (required)")
        @NotBlank(message = "Email cannot be blank")
        @Size(min = 3, max = 64, message = "Email must be between 3 and 64 characters")
        String email,
        @Schema(description = "The gender of the user(required)", allowableValues = {"MALE", "FEMALE"})
        Gender gender,
        @Schema(description = "The description of the user (required)")
        @NotBlank(message = "Description cannot be blank")
        @Size(min = 2, max = 512, message = "Description must be between 2 and 512 characters")
        String description
) {
    public UserUpdateRequest(String name, String email, Gender gender, String description) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.description = description;
    }
}
