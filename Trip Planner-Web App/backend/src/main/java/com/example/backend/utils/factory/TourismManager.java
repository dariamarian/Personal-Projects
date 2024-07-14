package com.example.backend.utils.factory;

import com.example.backend.service.AttractionService;
import com.example.backend.service.HotelService;
import com.example.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourismManager {
    private final HotelService hotelService;
    private final AttractionService attractionService;
    private final RestaurantService restaurantService;

    @Autowired
    public TourismManager(ServiceFactory serviceFactory) {
        this.hotelService = serviceFactory.createHotelService();
        this.attractionService = serviceFactory.createAttractionService();
        this.restaurantService = serviceFactory.createRestaurantService();
    }

    /**
     * Parsează toate entitățile dintr-un răspuns pentru o anumită locație.
     * @param locationId ID-ul locației pentru care se parsează datele.
     * @param hotelResponseBody Răspunsul pentru hoteluri.
     * @param attractionResponseBody Răspunsul pentru atracții.
     * @param restaurantResponseBody Răspunsul pentru restaurante.
     */
    public void parseAllEntities(Long locationId, String hotelResponseBody, String attractionResponseBody, String restaurantResponseBody) {
        hotelService.parseEntities(locationId, hotelResponseBody);
        attractionService.parseEntities(locationId, attractionResponseBody);
        restaurantService.parseEntities(locationId, restaurantResponseBody);
    }
}

