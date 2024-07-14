package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Hotel;
import com.example.backend.model.Restaurant;
import com.example.backend.service.EntityService;
import com.example.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/locations")
@OpenAPIDefinition(info = @Info(title = "Travel API", version = "v1"))
@Validated
public class RestaurantController {
    @Value("${travel-api-key}")
    private String apiKey;

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public void parseRestaurants(@RequestParam String locationId) {
        String apiUrl = "https://travel-advisor.p.rapidapi.com/restaurants/list?location_id=" + locationId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            restaurantService.parseEntities(Long.parseLong(locationId), response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Get all restaurants for a locationId", description = "This endpoint is used to retrieve all " +
            "restaurants for a specific location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants found successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Restaurant.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@PathVariable("locationId") Long locationId) throws NotFoundException {
        List<Restaurant> restaurants = restaurantService.getEntitiesByLocationId(locationId);
        return ResponseEntity.ok(restaurants);
    }

    @Operation(summary = "Sort restaurants", description = "This endpoint is used to sort restaurants by rating or name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants sorted successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Restaurant.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/restaurants/sort")
    public ResponseEntity<List<Restaurant>> sortRestaurants(@PathVariable("locationId") Long locationId,
                                                            @RequestParam("strategy") String strategy)
            throws NotFoundException {
        List<Restaurant> restaurants = restaurantService.sortEntities(locationId, strategy);
        return ResponseEntity.ok(restaurants);
    }
}
