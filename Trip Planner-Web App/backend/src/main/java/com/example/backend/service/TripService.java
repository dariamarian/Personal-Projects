package com.example.backend.service;

import com.example.backend.controller.OpenAI;
import com.example.backend.dto.CuisineRequest;
import com.example.backend.dto.trip.TripRequest;
import com.example.backend.dto.trip.TripResponse;
import com.example.backend.exception.AlreadyExistsException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.FavoriteTripRepo;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.TripRepo;
import com.example.backend.repository.UserRepo;
import com.example.backend.utils.mapper.TripMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final TripRepo tripRepo;
    private final FavoriteTripRepo favoriteTripRepo;
    private final UserRepo userRepo;
    private final LocationRepo locationRepo;
    private final OpenAI openAI;

    public TripService(TripRepo tripRepo, FavoriteTripRepo favoriteTripRepo, UserRepo userRepo,
                       LocationRepo locationRepo, OpenAI openAI) {
        this.tripRepo = tripRepo;
        this.favoriteTripRepo = favoriteTripRepo;
        this.userRepo = userRepo;
        this.locationRepo = locationRepo;
        this.openAI = openAI;
    }

    public Trip addTrip(TripRequest tripRequest) throws NotFoundException {
        User user = userRepo.findById(tripRequest.userId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + tripRequest.userId()));
        List<Attraction> attractions = tripRequest.attractions().stream()
                .map(attraction -> new Attraction(
                        attraction.name(),
                        attraction.rating(),
                        attraction.url(),
                        attraction.latitude(),
                        attraction.longitude(),
                        locationRepo.findByLocationId(attraction.locationId()).orElse(null)
                ))
                .collect(Collectors.toList());
        List<Restaurant> restaurants = tripRequest.restaurants().stream()
                .map(restaurant -> new Restaurant(
                        restaurant.name(),
                        restaurant.rating(),
                        restaurant.phone(),
                        restaurant.email(),
                        restaurant.url(),
                        restaurant.priceLevel(),
                        fromCuisineRequests(restaurant.cuisine()),
                        restaurant.latitude(),
                        restaurant.longitude(),
                        locationRepo.findByLocationId(restaurant.locationId()).orElse(null)
                ))
                .collect(Collectors.toList());
        List<Hotel> hotels = tripRequest.hotels().stream()
                .map(hotel -> new Hotel(
                        hotel.name(),
                        hotel.rating(),
                        hotel.url(),
                        hotel.priceLevel(),
                        hotel.price(),
                        hotel.latitude(),
                        hotel.longitude(),
                        locationRepo.findByLocationId(hotel.locationId()).orElse(null)
                ))
                .collect(Collectors.toList());
        Trip tripToSave = new Trip.TripBuilder(tripRequest.city(), tripRequest.startDate(), tripRequest.endDate(), user)
                .setAttractions(attractions)
                .setRestaurants(restaurants)
                .setHotels(hotels)
                .build();
        return tripRepo.save(tripToSave);
    }

    public List<String> fromCuisineRequests(List<CuisineRequest> cuisineRequests) {
        List<String> cuisines = new ArrayList<>();
        for (CuisineRequest cuisineRequest : cuisineRequests) {
            cuisines.add(cuisineRequest.name());
        }
        return cuisines;
    }

    public TripResponse getTripById(Long id) {
        Trip trip = tripRepo.findById(id).orElse(null);
        assert trip != null;
        return TripMapper.entityToDto(trip);
    }

    public void deleteTripById(Long id) {
        tripRepo.deleteByTripId(id);
    }

    public List<TripResponse> getAllTripsByUserId(Long userId) {
        List<Trip> trips = tripRepo.findAllByUserId(userId);
        return TripMapper.entityListToDto(trips);
    }

    public void addTripToFavorites(FavoriteTrip favoriteTrip) {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepo.findByIdUserAndIdTrip(favoriteTrip.getIdUser(),
                favoriteTrip.getIdTrip());
        if (favoriteTripOptional.isPresent()) {
            throw new AlreadyExistsException("Trip already added to favorites");
        } else {
            favoriteTripRepo.save(favoriteTrip);
        }
    }

    public List<TripResponse> getFavoriteTrips(Long idUser) {
        List<FavoriteTrip> favoriteTrips = favoriteTripRepo.findAllByIdUser(idUser);
        List<TripResponse> trips = new ArrayList<>();
        for (FavoriteTrip favoriteTrip : favoriteTrips) {
            Trip trip = tripRepo.findById(favoriteTrip.getIdTrip()).orElseThrow();
            trips.add(TripMapper.entityToDto(trip));
        }
        return trips;
    }

    public void removeTripFromFavorites(FavoriteTrip favoriteTrip) {
        FavoriteTrip favoriteTripToDelete = favoriteTripRepo.findByIdUserAndIdTrip(favoriteTrip.getIdUser(),
                favoriteTrip.getIdTrip()).orElseThrow();
        favoriteTripRepo.delete(favoriteTripToDelete);
    }

    public List<Trip> getUserSuggestions(Long userId) {
        List<Trip> trips = tripRepo.findAllByUserId(userId);
        List<Trip> lastThreeTrips;
        if (trips.size() > 3) {
            lastThreeTrips = trips.subList(trips.size() - 3, trips.size());
        } else {
            lastThreeTrips = trips;
        }
        StringBuilder script = new StringBuilder("These are a person's last three trips: ");
        for (Trip trip : lastThreeTrips) {
            script.append("The user went to ").append(trip.getCity()).append(" from ").append(trip.getStartDate())
                    .append(" to ").append(trip.getEndDate()).append(". ");
        }
        script.append("Please analyze the cities the user chose, the duration (if he likes citybreaks or long holidays) " +
                "and then I want you to give me IN JSON FORMAT (city, start_date, end_date) 5 new trips to different " +
                "cities which would match this users preferences. The date must be in the future. Don't say anything else," +
                " i want the response to contain only the 5 json objects with the 3 values each");
        String response = openAI.generateChatGPTResponse(script.toString());
        System.out.println(response);
        int startIndex = response.indexOf('[');
        int endIndex = response.lastIndexOf(']') + 1; // +1 to include the last bracket

        if (startIndex != -1 && endIndex != -1) {
            String jsonString = response.substring(startIndex, endIndex);
            JSONArray tripArray = new JSONArray(jsonString);
            List<Trip> suggestedTrips = new ArrayList<>();
            for (int i = 0; i < tripArray.length(); i++) {
                JSONObject tripData = tripArray.getJSONObject(i);
                String city = tripData.getString("city");
                LocalDate startDate = LocalDate.parse(tripData.getString("start_date"));
                LocalDate endDate = LocalDate.parse(tripData.getString("end_date"));

                Trip newTrip = new Trip.TripBuilder(city, startDate, endDate, userRepo.findById(userId).orElseThrow())
                        .build();
                suggestedTrips.add(newTrip);
            }
            return suggestedTrips;

        } else {
            throw new RuntimeException("Unable to extract JSON content from response: " + response);
        }
    }
}
