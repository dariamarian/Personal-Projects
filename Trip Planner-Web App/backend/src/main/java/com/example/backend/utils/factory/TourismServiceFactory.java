package com.example.backend.utils.factory;

import com.example.backend.repository.AttractionRepo;
import com.example.backend.repository.HotelRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.RestaurantRepo;
import com.example.backend.service.AttractionService;
import com.example.backend.service.HotelService;
import com.example.backend.service.RestaurantService;
import com.example.backend.utils.adapter.AttractionAdapter;
import com.example.backend.utils.adapter.HotelAdapter;
import com.example.backend.utils.adapter.RestaurantAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourismServiceFactory implements ServiceFactory {
    private final HotelRepo hotelRepo;
    private final AttractionRepo attractionRepo;
    private final RestaurantRepo restaurantRepo;
    private final LocationRepo locationRepo;
    private final HotelAdapter hotelAdapter;
    private final AttractionAdapter attractionAdapter;
    private final RestaurantAdapter restaurantAdapter;

    @Autowired
    public TourismServiceFactory(HotelRepo hotelRepo, AttractionRepo attractionRepo, RestaurantRepo restaurantRepo,
                                 LocationRepo locationRepo, HotelAdapter hotelAdapter, AttractionAdapter attractionAdapter,
                                 RestaurantAdapter restaurantAdapter) {
        this.hotelRepo = hotelRepo;
        this.attractionRepo = attractionRepo;
        this.restaurantRepo = restaurantRepo;
        this.locationRepo = locationRepo;
        this.hotelAdapter = hotelAdapter;
        this.attractionAdapter = attractionAdapter;
        this.restaurantAdapter = restaurantAdapter;
    }

    @Override
    public HotelService createHotelService() {
        return new HotelService(hotelRepo, locationRepo, hotelAdapter);
    }

    @Override
    public AttractionService createAttractionService() {
        return new AttractionService(attractionRepo, locationRepo, attractionAdapter);
    }

    @Override
    public RestaurantService createRestaurantService() {
        return new RestaurantService(restaurantRepo, locationRepo, restaurantAdapter);
    }
}
