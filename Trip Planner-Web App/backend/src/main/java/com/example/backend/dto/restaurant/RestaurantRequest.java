package com.example.backend.dto.restaurant;

import com.example.backend.dto.CuisineRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestaurantRequest(
        @Schema(description = "The name of the restaurant")
        @NotNull(message = "Name cannot be null")
        String name,

        @Schema(description = "The rating of the restaurant")
        double rating,

        @Schema(description = "The phone number of the restaurant")
        String phone,

        @Schema(description = "The email address of the restaurant")
        String email,

        @Schema(description = "The url of the site of the restaurant")
        String url,

        @Schema(description = "The price level of the restaurant")
        String priceLevel,

        @Schema(description = "The cuisine of the restaurant")
        List<CuisineRequest> cuisine,

        @Schema(description = "The latitude of the restaurant")
        double latitude,

        @Schema(description = "The longitude of the restaurant")
        double longitude,

        @Schema(description = "The id of the location of the restaurant")
        @NotNull(message = "Location Id cannot be null")
        Long locationId
) {
}
