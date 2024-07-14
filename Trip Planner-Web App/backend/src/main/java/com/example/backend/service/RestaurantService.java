package com.example.backend.service;

import com.example.backend.model.Restaurant;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.RestaurantRepo;
import com.example.backend.utils.adapter.RestaurantAdapter;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService extends EntityService<Restaurant> {
    public RestaurantService(RestaurantRepo entityRepo, LocationRepo locationRepo, RestaurantAdapter entityAdapter) {
        super(entityRepo, locationRepo, entityAdapter);
    }
}
