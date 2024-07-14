package com.example.backend.model;

import com.example.backend.utils.strategy.Sortable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "hotel")
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Hotel implements Sortable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the hotel")
    private Long hotel_id;

    @Schema(description = "The name of the hotel")
    private String name;

    @Schema(description = "The rating of the hotel")
    private double rating;

    @Schema(description = "The url of the site of the hotel")
    private String url;

    @Schema(description = "The price level of the hotel")
    private String price_level;

    @Schema(description = "The price of the hotel")
    private String price;

    @Schema(description = "The latitude of the hotel")
    private double latitude;

    @Schema(description = "The longitude of the hotel")
    private double longitude;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "location_id")
    @Schema(description = "The location of the hotel")
    private Location location;

    public Hotel(String name, double rating, String url, String price_level, String price, double latitude, double longitude, Location location) {
        this.name = name;
        this.rating = rating;
        this.url = url;
        this.price_level = price_level;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }
}
