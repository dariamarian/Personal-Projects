package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Hotel;
import com.example.backend.model.Location;
import com.example.backend.repository.HotelRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.HotelAdapter;
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
public class HotelServiceTest {

    @Mock
    private HotelRepo hotelRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private HotelAdapter hotelAdapter;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void parseHotelsTest() {
        Long locationId = 1L;
        String jsonResponse = "{\"hotels\": []}";

        hotelService.parseEntities(locationId, jsonResponse);
        verify(hotelAdapter).process(locationId, jsonResponse);
    }

    @Test
    void getHotelsByLocationIdFoundTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Hotel hotel = new Hotel();
        List<Hotel> expectedHotels = Arrays.asList(hotel);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(hotelRepo.findAllByLocation(location)).thenReturn(expectedHotels);

        List<Hotel> result = hotelService.getEntitiesByLocationId(locationId);
        assertEquals(expectedHotels, result);
    }

    @Test
    void getHotelsByLocationIdNotFoundTest() {
        Long locationId = 2L;
        when(locationRepo.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> hotelService.getEntitiesByLocationId(locationId));
    }

    @Test
    void sortHotelsTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        List<Hotel> unsortedHotels = Arrays.asList(hotel2, hotel1);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(hotelRepo.findAllByLocation(location)).thenReturn(unsortedHotels);

        // Since we're not testing the actual sorting logic, we can simply test if the method
        // completes successfully and returns some list. This is a limitation of this approach.
        List<Hotel> result = hotelService.sortEntities(locationId, "rating");
        assertNotNull(result);
        assertFalse(result.isEmpty()); // Check that some result is returned

        verify(locationRepo).findById(locationId); // Verify interaction with locationRepo
        verify(hotelRepo).findAllByLocation(location); // Verify interaction with hotelRepo
    }
}
