package com.example.backend.repository;

import com.example.backend.model.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class LocationRepoTest {

    @MockBean
    private LocationRepo locationRepo;

    @Test
    void findByName() {
        // Setup
        String locationName = "Paris";
        Location expectedLocation = new Location();
        expectedLocation.setName(locationName);
        expectedLocation.setLocation_id(1L);

        Mockito.when(locationRepo.findByName(locationName)).thenReturn(expectedLocation);

        // Execution
        Location result = locationRepo.findByName(locationName);

        // Verify
        assertThat(result).isEqualTo(expectedLocation);
    }

    @Test
    void findByLocationId() {
        // Setup
        Long locationId = 1L;
        Location expectedLocation = new Location();
        expectedLocation.setLocation_id(locationId);
        expectedLocation.setName("Paris");

        Mockito.when(locationRepo.findByLocationId(locationId)).thenReturn(Optional.of(expectedLocation));

        // Execution
        Optional<Location> result = locationRepo.findByLocationId(locationId);

        // Verify
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedLocation);
    }
}