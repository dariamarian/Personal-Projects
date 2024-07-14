package com.example.backend.model;

import com.example.backend.utils.strategy.Sortable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "attraction")
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Attraction implements Sortable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the attraction")
    private Long attraction_id;

    @Schema(description = "The name of the attraction")
    private String name;

    @Schema(description = "The rating of the attraction")
    private double rating;

    @Schema(description = "The url of the site of the attraction")
    private String url;

    @Schema(description = "The latitude of the attraction")
    private double latitude;

    @Schema(description = "The longitude of the attraction")
    private double longitude;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "location_id")
    @Schema(description = "The location of the attraction")
    private Location location;

    public Attraction(String name, double rating, String url, double latitude, double longitude, Location location) {
        this.name = name;
        this.rating = rating;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public String getPrice() {
        return "";
    }
}
