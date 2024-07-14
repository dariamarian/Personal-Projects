package com.example.backend.dto.attraction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AttractionRequest(
        @Schema(description = "The name of the attraction")
        @NotNull(message = "Name cannot be null")
        String name,

        @Schema(description = "The rating of the attraction")
        double rating,

        @Schema(description = "The url of the site of the attraction")
        String url,

        @Schema(description = "The latitude of the attraction")
        double latitude,

        @Schema(description = "The longitude of the attraction")
        double longitude,

        @Schema(description = "The id of the location of the attraction")
        @NotNull(message = "Location Id cannot be null")
        Long locationId
) {
}
