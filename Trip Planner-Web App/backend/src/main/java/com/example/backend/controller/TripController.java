package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.trip.TripRequest;
import com.example.backend.dto.trip.TripResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.FavoriteTrip;
import com.example.backend.model.Trip;
import com.example.backend.service.TripService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@OpenAPIDefinition(info = @Info(title = "Travel API", version = "v1"))
@Validated
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Create a new trip", description = "This endpoint is used to create a new trip." +
            "The details of the trip to be created are passed in the request body. " +
            "The response body contains the details of the created trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Trip.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request due to validation errors",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @PostMapping("/trips")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody TripRequest tripRequest) throws NotFoundException {
        Trip createdTrip = tripService.addTrip(tripRequest);
        return ResponseEntity.ok(createdTrip);
    }

    @Operation(summary = "Delete a trip", description = "This endpoint is used to delete an existing trip.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trip deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "The trip with the given id does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable("tripId") Long tripId) {
        tripService.deleteTripById(tripId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get trip with specified id", description = "This endpoint is used to retrieve a trip with " +
            "specified id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Trip.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable("tripId") Long tripId) {
        TripResponse trip = tripService.getTripById(tripId);
        return ResponseEntity.ok(trip);
    }

    @Operation(summary = "Get all trips for a user", description = "This endpoint is used to retrieve all trips for a " +
            "specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Trip.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{userId}/trips")
    public ResponseEntity<List<TripResponse>> getAllTrips(@PathVariable("userId") Long userId) {
        List<TripResponse> trips = tripService.getAllTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    @Operation(summary = "Add a trip to favorites", description = "This endpoint is used to add a trip to favorites. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip added to favorites successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @PostMapping("/trips/favorites")
    public ResponseEntity<ResponseDto> addTripToFavorites(@Valid @RequestBody FavoriteTrip favoriteTrip) {
        tripService.addTripToFavorites(favoriteTrip);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a trip from favorites", description = "This endpoint is used to remove a trip from favorites. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip removed from favorites successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @DeleteMapping("/trips/favorites")
    public ResponseEntity<ResponseDto> removeTripFromFavorites(@Valid @RequestBody FavoriteTrip favoriteTrip) {
        tripService.removeTripFromFavorites(favoriteTrip);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all favorite trips for user", description = "This endpoint is used to retrieve all favorite" +
            " trips of a user. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips found successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Trip.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{userId}/trips/favorites")
    public ResponseEntity<List<TripResponse>> getAllFavoriteTrips(@Parameter(description = "User id", required = true)
                                                                  @PathVariable("userId") Long userId) {
        List<TripResponse> trips = tripService.getFavoriteTrips(userId);
        return ResponseEntity.ok(trips);
    }

    @Operation(summary = "Get suggestions based on the last 3 trips", description = "This endpoint is used to retrieve " +
            "user suggestions based on the last 3 trips.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User suggestions generated successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Trip.class)))}),
            @ApiResponse(responseCode = "404", description = "The user with the given id does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/{userId}/suggestions")
    public ResponseEntity<List<Trip>> getUserSuggestions(@PathVariable("userId") Long userId) {
        List<Trip> trips = tripService.getUserSuggestions(userId);
        return ResponseEntity.ok(trips);
    }
}
