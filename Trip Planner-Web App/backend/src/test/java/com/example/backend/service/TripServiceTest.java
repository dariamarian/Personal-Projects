package com.example.backend.service;

import com.example.backend.controller.OpenAI;
import com.example.backend.dto.trip.TripRequest;
import com.example.backend.dto.trip.TripResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.FavoriteTrip;
import com.example.backend.model.Location;
import com.example.backend.model.Trip;
import com.example.backend.model.User;
import com.example.backend.repository.FavoriteTripRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.TripRepo;
import com.example.backend.repository.UserRepo;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepo tripRepo;
    @Mock
    private FavoriteTripRepo favoriteTripRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private OpenAI openAI;
    @InjectMocks
    private TripService tripService;

    @Test
    void addTripShouldSaveTrip() throws NotFoundException {
        // Setup
        User user = new User();
        user.setId(1L);
        TripRequest tripRequest = createTripRequest();
        lenient().when(userRepo.findById(tripRequest.userId())).thenReturn(Optional.of(user));
        lenient().when(locationRepo.findByLocationId(anyLong())).thenReturn(Optional.of(new Location()));

        // Mocking the tripRepo to return the trip it receives
        when(tripRepo.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Action
        Trip result = tripService.addTrip(tripRequest);

        // Assertion
        assertNotNull(result);
        verify(tripRepo).save(any(Trip.class));
    }

    @Test
    void addTripShouldFailWhenUserNotFound() {
        TripRequest tripRequest = createTripRequest();
        when(userRepo.findById(tripRequest.userId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tripService.addTrip(tripRequest));
    }

    @Test
    void getTripByIdShouldReturnTrip() {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setTrip_id(1L);
        trip.setUser(user);

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));

        TripResponse result = tripService.getTripById(1L);

        assertNotNull(result);
        assertEquals(1L, result.trip_id());
    }

    @Test
    void deleteTripByIdShouldInvokeRepo() {
        lenient().doNothing().when(tripRepo).deleteById(1L);

        tripService.deleteTripById(1L);

        verify(tripRepo).deleteByTripId(1L);
    }

    @Test
    void getAllTripsByUserIdShouldReturnList() {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setUser(user);

        Trip trip2 = new Trip();
        trip2.setUser(user);

        List<Trip> trips = Arrays.asList(trip, trip2);
        when(tripRepo.findAllByUserId(1L)).thenReturn(trips);

        List<TripResponse> results = tripService.getAllTripsByUserId(1L);

        assertEquals(2, results.size());
    }

    @Test
    void addTripToFavoritesShouldSaveWhenNotPresent() {
        lenient().when(favoriteTripRepo.findByIdUserAndIdTrip(anyLong(), anyLong())).thenReturn(Optional.empty());

        tripService.addTripToFavorites(new FavoriteTrip());

        verify(favoriteTripRepo).save(any(FavoriteTrip.class));
    }

    private TripRequest createTripRequest() {
        return new TripRequest("New York", LocalDate.now(), LocalDate.now().plusDays(5),
                1L, Arrays.asList(), Arrays.asList(), Arrays.asList());
    }

    @Test
    void getFavoriteTripsShouldReturnTrips() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Trip trip = new Trip();
        trip.setUser(user);
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setIdTrip(1L);
        when(favoriteTripRepo.findAllByIdUser(userId)).thenReturn(Arrays.asList(favoriteTrip));
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));

        List<TripResponse> results = tripService.getFavoriteTrips(userId);

        assertEquals(1, results.size());
        verify(tripRepo).findById(1L);
    }

    @Test
    void removeTripFromFavoritesShouldDeleteFavorite() {
        Long userId = 1L;
        Long tripId = 1L;
        FavoriteTrip favoriteTrip = new FavoriteTrip(userId, tripId);
        when(favoriteTripRepo.findByIdUserAndIdTrip(userId, tripId)).thenReturn(Optional.of(favoriteTrip));
        doNothing().when(favoriteTripRepo).delete(favoriteTrip);

        tripService.removeTripFromFavorites(favoriteTrip);

        verify(favoriteTripRepo).delete(favoriteTrip);
    }

    @Test
    void getUserSuggestionsShouldReturnSuggestions() throws JSONException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(openAI.generateChatGPTResponse(anyString())).thenReturn("[{\"city\":\"New York\",\"start_date\":\"2024-06-01\",\"end_date\":\"2024-06-05\"}]");
        when(tripRepo.findAllByUserId(userId)).thenReturn(Arrays.asList(new Trip(), new Trip(), new Trip()));

        List<Trip> suggestedTrips = tripService.getUserSuggestions(userId);

        assertEquals(1, suggestedTrips.size());
        assertEquals("New York", suggestedTrips.get(0).getCity());
        assertEquals(LocalDate.of(2024, 6, 1), suggestedTrips.get(0).getStartDate());
    }
}
