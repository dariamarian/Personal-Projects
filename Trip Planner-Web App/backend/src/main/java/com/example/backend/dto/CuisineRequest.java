package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CuisineRequest(
        @NotNull(message = "Name cannot be null")
        @Schema(description = "The name of the cuisine")
        String name,

        @Schema(description = "The id of the restaurant")
        Long restaurant_id
) {
}
