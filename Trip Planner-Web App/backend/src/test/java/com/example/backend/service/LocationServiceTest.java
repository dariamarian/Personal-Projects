package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Location;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.LocationAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private LocationAdapter locationAdapter;

    @InjectMocks
    private LocationService locationService;

    @Test
    void findByNameFound() throws NotFoundException {
        Location location = new Location();
        location.setName("Paris");
        when(locationRepo.findByName("Paris")).thenReturn(location);

        Location foundLocation = locationService.findByName("Paris");

        assertNotNull(foundLocation);
        assertEquals("Paris", foundLocation.getName());
        verify(locationRepo).findByName("Paris");
    }

    @Test
    void findByIdFound() throws NotFoundException {
        Location location = new Location();
        location.setLocation_id(1L);
        when(locationRepo.findByLocationId(1L)).thenReturn(Optional.of(location));

        Location foundLocation = locationService.findById("1");

        assertNotNull(foundLocation);
        assertEquals(1L, foundLocation.getLocation_id());
    }

    @Test
    void findByIdNotFound() {
        when(locationRepo.findByLocationId(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> locationService.findById("99"));
    }

    @Test
    void getAllLocationsTest() {
        Location location1 = new Location();
        Location location2 = new Location();
        List<Location> locations = Arrays.asList(location1, location2);
        when(locationRepo.findAll()).thenReturn(locations);

        List<Location> fetchedLocations = locationService.getAllLocations();

        assertEquals(2, fetchedLocations.size());
        verify(locationRepo).findAll();
    }

    @Test
    void parseLocationTest() {
        String json = "{}"; // Example JSON string
        doNothing().when(locationAdapter).process(json);

        locationService.parseLocation(json);

        verify(locationAdapter).process(json);
    }
}
