package com.example.backend.controller;

import com.example.backend.utils.factory.TourismManager;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/locations/all-entities")
@OpenAPIDefinition(info = @Info(title = "Travel API", version = "v1"))
@Validated
public class TourismController {
    @Value("${travel-api-key}")
    private String apiKey;

    private final TourismManager tourismManager;

    public TourismController(TourismManager tourismManager) {
        this.tourismManager = tourismManager;
    }

    @GetMapping("/parse")
    public void parseEntities(@RequestParam String locationId) {
        String attractionsApiUrl = "https://travel-advisor.p.rapidapi.com/attractions/list?location_id=" + locationId;
        String hotelsApiUrl = "https://travel-advisor.p.rapidapi.com/hotels/list?location_id=" + locationId;
        String restaurantsApiUrl = "https://travel-advisor.p.rapidapi.com/restaurants/list?location_id=" + locationId;
        HttpRequest attractionsRequest = HttpRequest.newBuilder()
                .uri(URI.create(attractionsApiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpRequest hotelsRequest = HttpRequest.newBuilder()
                .uri(URI.create(hotelsApiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpRequest restaurantsRequest = HttpRequest.newBuilder()
                .uri(URI.create(restaurantsApiUrl))
                .header("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                .header("X-RapidAPI-Key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> attractionsResponse;
        HttpResponse<String> hotelsResponse;
        HttpResponse<String> restaurantsResponse;
        try {
            attractionsResponse = HttpClient.newHttpClient().send(attractionsRequest, HttpResponse.BodyHandlers.ofString());
            hotelsResponse = HttpClient.newHttpClient().send(hotelsRequest, HttpResponse.BodyHandlers.ofString());
            restaurantsResponse = HttpClient.newHttpClient().send(restaurantsRequest, HttpResponse.BodyHandlers.ofString());
            tourismManager.parseAllEntities(Long.parseLong(locationId), hotelsResponse.body(), attractionsResponse.body(), restaurantsResponse.body());
        } catch (IOException |
                 InterruptedException e) {
            e.printStackTrace();
        }
    }
}
