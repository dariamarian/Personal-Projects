package com.example.backend.service;

import com.example.backend.model.Hotel;
import com.example.backend.repository.HotelRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.HotelAdapter;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends EntityService<Hotel> {
    public HotelService(HotelRepo entityRepo, LocationRepo locationRepo, HotelAdapter entityAdapter) {
        super(entityRepo, locationRepo, entityAdapter);
    }
}
