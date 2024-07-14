package com.example.backend.repository;

import com.example.backend.model.Attraction;
import com.example.backend.model.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AttractionRepoTest {

    @MockBean
    private AttractionRepo attractionRepo;

    @Test
    public void testFindAllByLocation() {
        // Setup
        Location location = new Location();
        location.setName("Test Location");

        Attraction attraction1 = new Attraction();
        attraction1.setName("Test Attraction 1");
        attraction1.setLocation(location);

        Attraction attraction2 = new Attraction();
        attraction2.setName("Test Attraction 2");
        attraction2.setLocation(location);

        List<Attraction> mockAttractions = Arrays.asList(attraction1, attraction2);

        Mockito.when(attractionRepo.findAllByLocation(any(Location.class))).thenReturn(mockAttractions);

        // Execution
        List<Attraction> attractions = attractionRepo.findAllByLocation(location);

        // Verify
        assertThat(attractions).hasSize(2);
        assertThat(attractions).containsExactlyInAnyOrder(attraction1, attraction2);
    }
}
