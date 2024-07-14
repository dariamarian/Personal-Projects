package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Attraction;
import com.example.backend.service.AttractionService;
import com.example.backend.service.EntityService;
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
public class AttractionController {
    @Value("${travel-api-key}")
    private String apiKey;

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/attractions")
    public void parseAttractions(@RequestParam String locationId) {
        String apiUrl = "https://travel-advisor.p.rapidapi.com/attractions/list?location_id=" + locationId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            attractionService.parseEntities(Long.parseLong(locationId), response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Get all attractions for a locationId", description = "This endpoint is used to retrieve all " +
            "attractions for a specific location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attractions found successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Attraction.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/attractions")
    public ResponseEntity<List<Attraction>> getAllAttractions(@PathVariable("locationId") Long locationId) throws NotFoundException {
        List<Attraction> attractions = attractionService.getEntitiesByLocationId(locationId);
        return ResponseEntity.ok(attractions);
    }

    @Operation(summary = "Sort attractions", description = "This endpoint is used to sort attractions by rating or name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attractions sorted successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Attraction.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/attractions/sort")
    public ResponseEntity<List<Attraction>> sortAttractions(@PathVariable("locationId") Long locationId,
                                                            @RequestParam("strategy") String strategy)
            throws NotFoundException {
        List<Attraction> attractions = attractionService.sortEntities(locationId, strategy);
        return ResponseEntity.ok(attractions);
    }
}
