package com.example.backend.utils.adapter;

import com.example.backend.model.Attraction;
import com.example.backend.model.Location;
import com.example.backend.repository.AttractionRepo;
import com.example.backend.repository.LocationRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttractionAdapter extends EntityAdapter<Attraction> implements DataProcessor{
    private final AttractionRepo attractionRepo;
    private final LocationRepo locationRepo;

    public void process(Long locationId, String jsonInput) {
        JSONObject json = new JSONObject(jsonInput);
        JSONArray dataArray = json.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject attractionObject = dataArray.getJSONObject(i);
            String name, rating, webUrl, latitude, longitude;
            if (!attractionObject.has("name")) {
                continue; // Skip this attraction if it doesn't have a name
            }

            Location location = locationRepo.findByLocationId(locationId).orElse(null);
            if (location == null) {
                continue; // Skip this attraction if location is not found
            }
            name = attractionObject.getString("name");

            rating = attractionObject.optString("rating", "0");
            webUrl = attractionObject.optString("web_url", "N/A");
            latitude = attractionObject.optString("latitude", "0");
            longitude = attractionObject.optString("longitude", "0");

            Attraction attraction = new Attraction(name, Double.parseDouble(rating), webUrl,
                    Double.parseDouble(latitude), Double.parseDouble(longitude), location);
            attractionRepo.save(attraction);
        }
    }
}
