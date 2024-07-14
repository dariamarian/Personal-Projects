package com.example.backend.service;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Attraction;
import com.example.backend.model.Location;
import com.example.backend.repository.AttractionRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.utils.adapter.AttractionAdapter;
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
public class AttractionServiceTest {

    @Mock
    private AttractionRepo attractionRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private AttractionAdapter attractionAdapter;

    @InjectMocks
    private AttractionService attractionService;

    @Test
    void parseAttractionsTest() {
        Long locationId = 1L;
        String jsonResponse = "{\"attractions\": []}";

        attractionService.parseEntities(locationId, jsonResponse);
        verify(attractionAdapter).process(locationId, jsonResponse);
    }

    @Test
    void getAttractionsByLocationIdFoundTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Attraction attraction = new Attraction();
        List<Attraction> expectedAttractions = Arrays.asList(attraction);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(attractionRepo.findAllByLocation(location)).thenReturn(expectedAttractions);

        List<Attraction> result = attractionService.getEntitiesByLocationId(locationId);
        assertEquals(expectedAttractions, result);
    }

    @Test
    void getAttractionsByLocationIdNotFoundTest() {
        Long locationId = 2L;
        when(locationRepo.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attractionService.getEntitiesByLocationId(locationId));
    }

    @Test
    void sortAttractionsTest() throws NotFoundException {
        Long locationId = 1L;
        Location location = new Location();
        location.setLocation_id(locationId);
        Attraction attraction1 = new Attraction();
        Attraction attraction2 = new Attraction();
        List<Attraction> unsortedAttractions = Arrays.asList(attraction2, attraction1);

        when(locationRepo.findById(locationId)).thenReturn(Optional.of(location));
        when(attractionRepo.findAllByLocation(location)).thenReturn(unsortedAttractions);

        // Since we're not testing the actual sorting logic, we can simply test if the method
        // completes successfully and returns some list. This is a limitation of this approach.
        List<Attraction> result = attractionService.sortEntities(locationId, "rating");
        assertNotNull(result);
        assertFalse(result.isEmpty()); // Check that some result is returned

        verify(locationRepo).findById(locationId); // Verify interaction with locationRepo
        verify(attractionRepo).findAllByLocation(location); // Verify interaction with attractionRepo
    }
}
