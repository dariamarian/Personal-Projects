package com.example.backend.utils.mapper;


import com.example.backend.dto.trip.TripResponse;
import com.example.backend.model.Trip;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class represents the mapper for the Trip entity.
 */
@Component
public final class TripMapper {
    public static TripResponse entityToDto(Trip trip) {
        return new TripResponse(trip.getTrip_id(), trip.getCity(), trip.getStartDate(), trip.getEndDate(),
                trip.getUser().getId(), trip.getAttractions(), trip.getRestaurants(), trip.getHotels());
    }

    public static List<TripResponse> entityListToDto(List<Trip> trips) {
        return trips.stream()
                .map(TripMapper::entityToDto)
                .toList();
    }
}
