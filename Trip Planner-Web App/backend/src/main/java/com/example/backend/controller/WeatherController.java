package com.example.backend.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@OpenAPIDefinition(info = @Info(title = "Travel API", version = "v1"))
@Validated
public class WeatherController {
    @Value("${weather-api-key}")
    private String apiKey;

    @GetMapping("/weather")
    public String getWeather(@RequestParam double lat, @RequestParam double lon) {
        String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&appid=" +
                apiKey + "&units=metric" + "&exclude=minutely";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
