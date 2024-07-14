package com.example.backend.repository;

import com.example.backend.model.Hotel;
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
public class HotelRepoTest {

    @MockBean
    private HotelRepo hotelRepo;

    @Test
    public void testFindAllByLocation() {
        // Setup
        Location location = new Location();
        location.setName("Test Location");

        Hotel hotel1 = new Hotel();
        hotel1.setName("Test Hotel 1");
        hotel1.setLocation(location);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Test Hotel 2");
        hotel2.setLocation(location);

        List<Hotel> mockHotels = Arrays.asList(hotel1, hotel2);
        Mockito.when(hotelRepo.findAllByLocation(any(Location.class))).thenReturn(mockHotels);

        // Execution
        List<Hotel> hotels = hotelRepo.findAllByLocation(location);

        // Verify
        assertThat(hotels).hasSize(2);
        assertThat(hotels).containsExactlyInAnyOrder(hotel1, hotel2);
    }
}
