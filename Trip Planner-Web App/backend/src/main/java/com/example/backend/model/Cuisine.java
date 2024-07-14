package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuisine")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the cuisine")
    private Long cuisine_id;

    @Schema(description = "The name of the cuisine")
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Cuisine(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }
}

