package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Hotel;
import com.example.backend.service.EntityService;
import com.example.backend.service.HotelService;
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
public class HotelController {
    @Value("${travel-api-key}")
    private String apiKey;

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public void parseHotels(@RequestParam String locationId) {
        String apiUrl = "https://travel-advisor.p.rapidapi.com/hotels/list?location_id=" + locationId + "&adults=1&rooms=1&nights=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            hotelService.parseEntities(Long.parseLong(locationId), response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Get all hotels for a locationId", description = "This endpoint is used to retrieve all " +
            "hotels for a specific location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels found successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Hotel.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels(@PathVariable("locationId") Long locationId) throws NotFoundException {
        List<Hotel> hotels = hotelService.getEntitiesByLocationId(locationId);
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Sort hotels", description = "This endpoint is used to sort hotels by rating, price or name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels sorted successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Hotel.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{locationId}/hotels/sort")
    public ResponseEntity<List<Hotel>> sortHotels(@PathVariable("locationId") Long locationId,
                                                  @RequestParam("strategy") String strategy) throws NotFoundException {
        List<Hotel> hotels = hotelService.sortEntities(locationId, strategy);
        return ResponseEntity.ok(hotels);
    }
}
