package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "trip")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the trip")
    private Long trip_id;

    @Schema(description = "The city of the trip")
    private String city;

    @Schema(description = "The start date of the trip")
    private LocalDate startDate;

    @Schema(description = "The end date of the trip")
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Schema(description = "The user who owns the trip")
    private User user;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "trip_attraction",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "attraction_id")}
    )
    @Schema(description = "The attractions of the trip")
    private List<Attraction> attractions;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "trip_restaurant",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant_id")}
    )
    @Schema(description = "The restaurants of the trip")
    private List<Restaurant> restaurants;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "trip_hotel",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "hotel_id")}
    )
    @Schema(description = "The hotels of the trip")
    private List<Hotel> hotels;

    public static class TripBuilder{
        private final String city;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final User user;
        private List<Attraction> attractions;
        private List<Restaurant> restaurants;
        private List<Hotel> hotels;

        public TripBuilder(String city, LocalDate startDate, LocalDate endDate, User user) {
            this.city = city;
            this.startDate = startDate;
            this.endDate = endDate;
            this.user = user;
        }

        public TripBuilder setAttractions(List<Attraction> attractions) {
            this.attractions = attractions;
            return this;
        }

        public TripBuilder setRestaurants(List<Restaurant> restaurants) {
            this.restaurants = restaurants;
            return this;
        }

        public TripBuilder setHotels(List<Hotel> hotels) {
            this.hotels = hotels;
            return this;
        }

        public Trip build() {
            return new Trip(this);
        }
    }

    private Trip(TripBuilder builder) {
        this.city = builder.city;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.user = builder.user;
        this.attractions = builder.attractions;
        this.restaurants = builder.restaurants;
        this.hotels = builder.hotels;
    }
}
