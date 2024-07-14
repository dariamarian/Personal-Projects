package com.example.backend.utils.adapter;

import com.example.backend.model.Location;
import com.example.backend.model.Restaurant;
import com.example.backend.repository.LocationRepo;
import com.example.backend.repository.RestaurantRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantAdapter extends EntityAdapter<Restaurant> implements DataProcessor {
    private final RestaurantRepo restaurantRepo;
    private final LocationRepo locationRepo;

    public void process(Long locationId, String jsonInput) {
        JSONObject json = new JSONObject(jsonInput);
        JSONArray dataArray = json.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject restaurantObject = dataArray.getJSONObject(i);
            String name, rating, phone, email, webUrl, priceLevel, latitude, longitude;
            if (!restaurantObject.has("name")) {
                continue; // Skip this restaurant if it doesn't have a name
            }

            Location location = locationRepo.findByLocationId(locationId).orElse(null);
            if (location == null) {
                continue; // Skip this restaurant if location is not found
            }
            name = restaurantObject.getString("name");

            rating = restaurantObject.optString("rating", "0");
            phone = restaurantObject.optString("phone", "N/A");
            email = restaurantObject.optString("email", "N/A");
            webUrl = restaurantObject.optString("website", "N/A");
            priceLevel = restaurantObject.optString("price_level", "N/A");

            List<String> cuisines = new ArrayList<>();
            if (!restaurantObject.has("cuisine")) {
                cuisines.add("N/A"); // Set the cuisine to "N/A" if it's not available
            } else {
                JSONArray cuisineArray = restaurantObject.getJSONArray("cuisine");
                for (int j = 0; j < cuisineArray.length(); j++) {
                    JSONObject cuisineObject = cuisineArray.getJSONObject(j);
                    String cuisineName = cuisineObject.getString("name");
                    if (cuisineName == null || cuisineName.isEmpty()) {
                        continue; // Skip this cuisine if it doesn't have a name
                    }
                    cuisines.add(cuisineName);
                }
            }
            latitude = restaurantObject.optString("latitude", "0");
            longitude = restaurantObject.optString("longitude", "0");

            Restaurant restaurant = new Restaurant(name, Double.parseDouble(rating), phone, email, webUrl,
                    priceLevel, cuisines, Double.parseDouble(latitude), Double.parseDouble(longitude), location);
            restaurantRepo.save(restaurant);
        }
    }
}
