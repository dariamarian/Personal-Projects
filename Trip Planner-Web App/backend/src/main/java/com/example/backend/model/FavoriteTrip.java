package com.example.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_trip")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "The id of the trip (required)")
    Long idTrip;
    @Schema(description = "The id of the user (required)")
    Long idUser;

    public FavoriteTrip(Long idTrip, Long idUser) {
        this.idTrip = idTrip;
        this.idUser = idUser;
    }
}
