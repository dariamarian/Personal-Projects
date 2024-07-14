package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Location;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.LocationAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represents the service for the Location entity.
 */
@Service
public class LocationService {
    private final LocationRepo locationRepo;
    private final LocationAdapter locationAdapter;

    public LocationService(LocationRepo locationRepo, LocationAdapter locationAdapter) {
        this.locationRepo = locationRepo;
        this.locationAdapter = locationAdapter;
    }

    public Location findByName(String name) throws NotFoundException {
        return locationRepo.findByName(name);
    }

    public Location findById(String locationId) throws NotFoundException {
        return locationRepo.findByLocationId(Long.parseLong(locationId)).orElseThrow(
                () -> new NotFoundException("Location not found with id: " + locationId));
    }

    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }

    public void parseLocation(String responseBody) {
        locationAdapter.process(responseBody);
    }
}
