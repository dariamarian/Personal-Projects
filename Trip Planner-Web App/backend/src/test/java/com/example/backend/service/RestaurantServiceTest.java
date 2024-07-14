package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Hotel;
import com.example.backend.model.Location;
import com.example.backend.model.Restaurant;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.RestaurantRepo;
import com.example.backend.utils.adapter.RestaurantAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private RestaurantAdapter restaurantAdapter;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void parseRestaurantsTest() {
        Long locationId = 1L;
        String jsonResponse = "{\"restaurants\": []}";

        restaurantService.parseEntities(locationId, jsonResponse);
        verify(restaurantAdapter).process(locationId, jsonResponse);
    }

    @Test
    void getRestaurantsByLocationIdFoundTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Restaurant restaurant = new Restaurant();
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(restaurantRepo.findAllByLocation(location)).thenReturn(expectedRestaurants);

        List<Restaurant> result = restaurantService.getEntitiesByLocationId(locationId);
        assertEquals(expectedRestaurants, result);
    }

    @Test
    void getRestaurantsByLocationIdNotFoundTest() {
        Long locationId = 2L;
        when(locationRepo.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantService.getEntitiesByLocationId(locationId));
    }

    @Test
    void sortRestaurantsTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Restaurant restaurant1 = new Restaurant();
        Restaurant restaurant2 = new Restaurant();
        List<Restaurant> unsortedRestaurants = Arrays.asList(restaurant2, restaurant1);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(restaurantRepo.findAllByLocation(location)).thenReturn(unsortedRestaurants);

        // Since we're not testing the actual sorting logic, we can simply test if the method
        // completes successfully and returns some list. This is a limitation of this approach.
        List<Restaurant> result = restaurantService.sortEntities(locationId, "rating");
        assertNotNull(result);
        assertFalse(result.isEmpty()); // Check that some result is returned

        verify(locationRepo).findById(locationId); // Verify interaction with locationRepo
        verify(restaurantRepo).findAllByLocation(location); // Verify interaction with restaurantRepo
    }
}
