package com.example.backend.model;

import com.example.backend.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents the user.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the user")
    private Long id;

    @Column(nullable = false, length = 64)
    @Schema(description = "The username of the user")
    private String username;

    @Column(nullable = false, length = 64)
    @Schema(description = "The name of the user")
    private String name;

    @Column(nullable = false, length = 64)
    @Schema(description = "The password of the user")
    private String password;

    @Column(nullable = false, length = 64)
    @Schema(description = "The email of the user")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "The gender of the user")
    private Gender gender;

    @Column(nullable = false)
    @Schema(description = "The date and time when the user was created")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Schema(description = "The description of the user")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Trip> trips;

    public User(String username, String name, String password, String email, Gender gender, LocalDateTime createdAt, String description) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.createdAt = createdAt;
        this.description = description;
    }

}
