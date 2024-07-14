package com.example.backend.repository;

import com.example.backend.model.Location;
import com.example.backend.model.Restaurant;
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
public class RestaurantRepoTest {

    @MockBean
    private RestaurantRepo restaurantRepo;

    @Test
    public void testFindAllByLocation() {
        // Setup
        Location location = new Location();
        location.setName("Test Location");

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Test Restaurant 1");
        restaurant1.setLocation(location);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Test Restaurant 2");
        restaurant2.setLocation(location);

        List<Restaurant> mockRestaurants = Arrays.asList(restaurant1, restaurant2);
        Mockito.when(restaurantRepo.findAllByLocation(any(Location.class))).thenReturn(mockRestaurants);

        // Execution
        List<Restaurant> restaurants = restaurantRepo.findAllByLocation(location);

        // Verify
        assertThat(restaurants).hasSize(2);
        assertThat(restaurants).containsExactlyInAnyOrder(restaurant1, restaurant2);
    }
}
