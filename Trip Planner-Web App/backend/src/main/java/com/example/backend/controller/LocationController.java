package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Location;
import com.example.backend.service.LocationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/locations")
@OpenAPIDefinition(info = @Info(title = "Travel API", version = "v1"))
@Validated
public class LocationController {
    @Value("${travel-api-key}")
    private String apiKey;
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Get all locations", description = "This endpoint is used to get all the locations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Location.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Get location by name", description = "This endpoint is used to get a location by its name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Location.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Location> getLocationByName(@PathVariable("name") String name) {
        try {
            Location location = locationService.findByName(name);
            return ResponseEntity.ok(location);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Parse the location", description = "This endpoint is used to parse a location ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location parsed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Location.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/location")
    public void parseLocation(@RequestParam String location) {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String apiUrl = "https://travel-advisor.p.rapidapi.com/locations/search?query=" + encodedLocation;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            locationService.parseLocation(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Get location by id", description = "This endpoint is used to get a location by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Location.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/id/{locationId}")
    public ResponseEntity<Location> getLocationById(@PathVariable("locationId") String locationId) {
        try {
            Location location = locationService.findById(locationId);
            return ResponseEntity.ok(location);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}