package com.example.backend.utils.adapter;

import com.example.backend.model.Location;
import com.example.backend.repository.LocationRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationAdapter implements LocationDataProcessor{
    private final LocationRepo locationRepo;

    public void process(String jsonInput) {
        try {
            JSONObject jsonObject = new JSONObject(jsonInput);
            if (!jsonObject.has("data")) {
                return;
            }
            JSONArray dataArray = jsonObject.getJSONArray("data");
            if (dataArray.isEmpty()) {
                return;
            }
            JSONObject locationObject = dataArray.getJSONObject(0);
            if (!locationObject.has("result_object")) {
                return;
            }
            JSONObject location = locationObject.getJSONObject("result_object");
            String locationId, name, latitude, longitude;
            if (!location.has("location_id") || !location.has("name") || !location.has("latitude") || !location.has("longitude")) {
                return; // Skip this location if it doesn't have a location_id, name, latitude, or longitude
            }
            locationId = location.getString("location_id");
            name = location.getString("name");
            latitude = location.getString("latitude");
            longitude = location.getString("longitude");
            Location locationToSave = new Location(Long.parseLong(locationId), name, Double.parseDouble(latitude), Double.parseDouble(longitude));
            locationRepo.save(locationToSave);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
