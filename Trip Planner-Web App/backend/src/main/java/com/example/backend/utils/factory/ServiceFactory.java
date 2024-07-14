package com.example.backend.utils.factory;

import com.example.backend.service.AttractionService;
import com.example.backend.service.HotelService;
import com.example.backend.service.RestaurantService;

public interface ServiceFactory {
    HotelService createHotelService();
    AttractionService createAttractionService();
    RestaurantService createRestaurantService();
}
