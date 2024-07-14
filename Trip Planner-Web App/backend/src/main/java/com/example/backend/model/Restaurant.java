package com.example.backend.model;

import com.example.backend.utils.strategy.Sortable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant implements Sortable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the restaurant")
    private Long restaurant_id;

    @Schema(description = "The name of the restaurant")
    private String name;

    @Schema(description = "The rating of the restaurant")
    private double rating;

    @Schema(description = "The phone number of the restaurant")
    private String phone;

    @Schema(description = "The email address of the restaurant")
    private String email;

    @Schema(description = "The url of the site of the restaurant")
    private String url;

    @Schema(description = "The price level of the restaurant")
    private String price_level;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "The cuisine of the restaurant")
    private List<Cuisine> cuisine = new ArrayList<>(); // Initialize the cuisine list

    @Schema(description = "The latitude of the restaurant")
    private double latitude;

    @Schema(description = "The longitude of the restaurant")
    private double longitude;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "location_id")
    @Schema(description = "The location of the restaurant")
    private Location location;

    public Restaurant(String name, double rating, String phone, String email, String url, String price_level,
                      List<String> cuisine, double latitude, double longitude, Location location) {
        this.name = name;
        this.rating = rating;
        this.phone = phone;
        this.email = email;
        this.url = url;
        this.price_level = price_level;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        for (String c : cuisine) {
            this.cuisine.add(new Cuisine(c, this));
        }
    }

    public String getPrice() {
        return "";
    }
}
