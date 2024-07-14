package com.example.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @Schema(description = "The id of the location")
    private Long location_id;

    @Schema(description = "The name of the location")
    private String name;

    @Schema(description = "The latitude of the location")
    private double latitude;

    @Schema(description = "The longitude of the location")
    private double longitude;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Schema(description = "The attractions of the location")
    private List<Attraction> attractions;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Schema(description = "The restaurants of the location")
    private List<Restaurant> restaurants;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Schema(description = "The hotels of the location")
    private List<Hotel> hotels;

    public Location(Long location_id, String name, double latitude, double longitude) {
        this.location_id = location_id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
