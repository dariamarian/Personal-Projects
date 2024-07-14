package com.example.backend.dto.trip;

import com.example.backend.model.Attraction;
import com.example.backend.model.Hotel;
import com.example.backend.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TripResponse(
        @Schema(description = "The id of the trip")
        Long trip_id,
        @Schema(description = "The city of the trip")
        String city,
        @Schema(description = "The start date of the trip")
        LocalDate startDate,
        @Schema(description = "The end date of the trip")
        LocalDate endDate,
        @Schema(description = "The id of the user who owns the trip")
        Long userId,
        @Schema(description = "The attractions of the trip")
        List<Attraction> attractions,
        @Schema(description = "The restaurants of the trip")
        List<Restaurant> restaurants,
        @Schema(description = "The hotels of the trip")
        List<Hotel> hotels
) {
}
