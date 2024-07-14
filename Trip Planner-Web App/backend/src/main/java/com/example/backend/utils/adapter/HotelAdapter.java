package com.example.backend.utils.adapter;

import com.example.backend.model.Hotel;
import com.example.backend.model.Location;
import com.example.backend.repository.HotelRepo;
import com.example.backend.repository.LocationRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelAdapter extends EntityAdapter<Hotel> implements DataProcessor {
    private final HotelRepo hotelRepo;
    private final LocationRepo locationRepo;

    public void process(Long locationId, String jsonInput) {
        JSONObject json = new JSONObject(jsonInput);
        JSONArray dataArray = json.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject hotelObject = dataArray.getJSONObject(i);
            String name, rating, webUrl, priceLevel, price, latitude, longitude;
            if (!hotelObject.has("name")) {
                continue; // Skip this hotel if it doesn't have a name
            }

            Location location = locationRepo.findByLocationId(locationId).orElse(null);
            if (location == null) {
                continue; // Skip this hotel if location is not found
            }
            name = hotelObject.getString("name");

            rating = hotelObject.optString("rating", "0");

            if (!hotelObject.has("business_listings")) {
                webUrl = "N/A"; // Set the webUrl to "N/A" if it's not available
            } else {
                JSONObject businessListingsObject = hotelObject.getJSONObject("business_listings");
                if (!businessListingsObject.has("mobile_contacts")) {
                    webUrl = "N/A"; // Set the webUrl to "N/A" if it's not available
                } else {
                    JSONArray mobileContactsArray = businessListingsObject.getJSONArray("mobile_contacts");
                    if (mobileContactsArray.isEmpty()) {
                        webUrl = "N/A"; // Set the webUrl to "N/A" if it's not available
                    } else {
                        JSONObject mobileContactsObject = mobileContactsArray.getJSONObject(0);
                        if (!mobileContactsObject.has("value")) {
                            webUrl = "N/A"; // Set the webUrl to "N/A" if it's not available
                        } else webUrl = mobileContactsObject.getString("value");
                    }
                }
            }

            priceLevel = hotelObject.optString("price_level", "N/A");
            price = hotelObject.optString("price", "N/A");
            latitude = hotelObject.optString("latitude", "0");
            longitude = hotelObject.optString("longitude", "0");

            Hotel hotel = new Hotel(name, Double.parseDouble(rating), webUrl, priceLevel, price,
                    Double.parseDouble(latitude), Double.parseDouble(longitude), location);
            hotelRepo.save(hotel);
        }
    }
}
