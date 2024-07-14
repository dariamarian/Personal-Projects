package com.example.backend.repository;

import com.example.backend.model.FavoriteTrip;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FavoriteTripRepoTest {

    @MockBean
    private FavoriteTripRepo favoriteTripRepo;

    @Test
    void findAllByIdUser() {
        // Setup
        Long userId = 1L;
        FavoriteTrip trip1 = new FavoriteTrip(1L, userId, 101L);
        FavoriteTrip trip2 = new FavoriteTrip(2L, userId, 102L);
        List<FavoriteTrip> expectedTrips = Arrays.asList(trip1, trip2);

        Mockito.when(favoriteTripRepo.findAllByIdUser(userId)).thenReturn(expectedTrips);

        // Execution
        List<FavoriteTrip> results = favoriteTripRepo.findAllByIdUser(userId);

        // Verify
        assertThat(results).hasSize(2);
        assertThat(results).containsExactlyInAnyOrderElementsOf(expectedTrips);
    }

    @Test
    void findByIdUserAndIdTrip() {
        // Setup
        Long userId = 1L;
        Long tripId = 101L;
        FavoriteTrip expectedTrip = new FavoriteTrip(1L, userId, tripId);

        Mockito.when(favoriteTripRepo.findByIdUserAndIdTrip(userId, tripId)).thenReturn(Optional.of(expectedTrip));

        // Execution
        Optional<FavoriteTrip> result = favoriteTripRepo.findByIdUserAndIdTrip(userId, tripId);

        // Verify
        assertTrue(result.isPresent());
        assertThat(result.get()).isEqualTo(expectedTrip);
    }
}
