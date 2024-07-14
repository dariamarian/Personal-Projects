package com.example.backend.dto.hotel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record HotelRequest(
        @Schema(description = "The name of the hotel")
        @NotNull(message = "Name cannot be null")
        String name,

        @Schema(description = "The rating of the hotel")
        double rating,

        @Schema(description = "The url of the site of the hotel")
        String url,

        @Schema(description = "The price level of the hotel")
        String priceLevel,

        @Schema(description = "The price of the hotel")
        String price,

        @Schema(description = "The latitude of the hotel")
        double latitude,

        @Schema(description = "The longitude of the hotel")
        double longitude,

        @Schema(description = "The id of the location of the hotel")
        @NotNull(message = "Location Id cannot be null")
        Long locationId
) {
}
