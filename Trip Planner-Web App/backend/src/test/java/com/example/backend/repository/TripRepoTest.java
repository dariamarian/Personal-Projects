package com.example.backend.repository;

import com.example.backend.model.Trip;
import com.example.backend.model.User;
import com.example.backend.model.enums.Gender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TripRepoTest {

    @MockBean
    private TripRepo tripRepo;

    @Test
    void findAllByUserId() {
        // Setup
        User user = new User(1L, "Test Username", "Test User", "Test Password",
                "Test Email", Gender.valueOf("FEMALE"), LocalDateTime.now(), "Test Description",
                new ArrayList<>());
        Trip trip1 = new Trip.TripBuilder("Test City", LocalDate.now(), LocalDate.now().plusDays(1), user).build();
        Trip trip2 = new Trip.TripBuilder("Test City 2", LocalDate.now(), LocalDate.now().plusDays(1), user).build();
        List<Trip> expectedTrips = Arrays.asList(trip1, trip2);

        Mockito.when(tripRepo.findAllByUserId(user.getId())).thenReturn(expectedTrips);

        // Execution
        List<Trip> results = tripRepo.findAllByUserId(user.getId());

        // Verify
        assertThat(results).hasSize(2);
        assertThat(results).containsExactlyInAnyOrderElementsOf(expectedTrips);
    }
}