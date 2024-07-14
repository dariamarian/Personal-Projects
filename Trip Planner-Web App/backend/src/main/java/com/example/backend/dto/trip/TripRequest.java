package com.example.backend.dto.trip;

import com.example.backend.dto.attraction.AttractionRequest;
import com.example.backend.dto.hotel.HotelRequest;
import com.example.backend.dto.restaurant.RestaurantRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TripRequest(
        @Schema(description = "The city of the trip")
        @NotNull(message = "City cannot be null")
        String city,
        @Schema(description = "The start date of the trip")
        @NotNull(message = "Start date cannot be null")
        LocalDate startDate,
        @Schema(description = "The end date of the trip")
        @NotNull(message = "End date cannot be null")
        LocalDate endDate,
        @Schema(description = "The id of the user who owns the trip")
        @NotNull(message = "User id cannot be null")
        Long userId,
        @Schema(description = "The attractions of the trip")
        @NotNull(message = "Attractions cannot be null")
        List<AttractionRequest> attractions,

        @Schema(description = "The restaurants of the trip")
        @NotNull(message = "Restaurants cannot be null")
        List<RestaurantRequest> restaurants,

        @Schema(description = "The hotels of the trip")
        @NotNull(message = "Hotels cannot be null")
        List<HotelRequest> hotels
) {
}
