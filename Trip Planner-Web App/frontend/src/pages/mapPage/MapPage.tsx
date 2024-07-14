import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {MapContainer, Marker, Popup, TileLayer} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import {AttractionService} from "@/apis/attraction/AttractionService";
import {Attraction, Hotel, Restaurant} from "@/utils/types";
import './MapPage.css';
import L from 'leaflet';
import {RestaurantService} from "@/apis/restaurant/RestaurantService.tsx";
import {HotelService} from "@/apis/hotel/HotelService.tsx";
import Navbar from "@/components/Navbar.tsx";

const orchidPin = new L.Icon({
    iconUrl: '/orchidPin.png',
    iconSize: [38, 31],
});

const blueGreenPin = new L.Icon({
    iconUrl: '/blueGreenPin.png',
    iconSize: [38, 31],
});

const redPin = new L.Icon({
    iconUrl: '/redPin.png',
    iconSize: [38, 31],
});

const MapPage: React.FC = () => {
    const {locationId} = useParams<{ locationId: string }>();
    const [attractions, setAttractions] = useState<Attraction[]>([]);
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [hotels, setHotels] = useState<Hotel[]>([]);
    const [mapCenter, setMapCenter] = useState<[number, number]>([0, 0]);
    const [selectedCuisine, setSelectedCuisine] = useState('');
    const [priceFilter, setPriceFilter] = useState({min: '', max: ''});
    const [allCuisines, setAllCuisines] = useState<string[]>([]);

    useEffect(() => {
        const fetchAttractions = async () => {
            if (locationId) {
                try {
                    let data = await AttractionService.getAttractions(locationId);
                    if (data.length === 0) {
                        await AttractionService.parseAttractions(locationId);
                        data = await AttractionService.getAttractions(locationId);
                    }
                    setAttractions(data);
                    if (data.length > 0) {
                        setMapCenter([data[0].latitude, data[0].longitude]);
                    }
                } catch (error) {
                    console.error("Error fetching attractions:", error);
                }
            }
        };

        const fetchRestaurants = async () => {
            if (locationId) {
                try {
                    let data = await RestaurantService.getRestaurants(locationId);
                    if (data.length === 0) {
                        await RestaurantService.parseRestaurants(locationId);
                        data = await RestaurantService.getRestaurants(locationId);
                    }
                    setRestaurants(data);
                    extractAndSetCuisines(data);
                } catch (error) {
                    console.error("Error fetching restaurants:", error);
                }
            }
        };

        const fetchHotels = async () => {
            if (locationId) {
                try {
                    let data = await HotelService.getHotels(locationId);
                    if (data.length === 0) {
                        await HotelService.parseHotels(locationId);
                        data = await HotelService.getHotels(locationId);
                    }
                    setHotels(data);
                } catch (error) {
                    console.error("Error fetching hotels:", error);
                }
            }
        };

        fetchAttractions();
        fetchRestaurants();
        fetchHotels();
    }, [locationId]);

    const extractAndSetCuisines = (restaurants: Restaurant[]) => {
        const uniqueCuisines = new Set<string>();
        restaurants.forEach((restaurant: Restaurant) => {
            restaurant.cuisine.forEach(c => {
                uniqueCuisines.add(c.name);
            });
        });
        setAllCuisines(Array.from(uniqueCuisines));
    };
    const filterRestaurants = () => {
        if (!selectedCuisine) return restaurants; // If no cuisine is selected, return all

        return restaurants.filter(restaurant =>
            restaurant.cuisine.some(c => c.name === selectedCuisine)
        );
    };


    const filterHotels = () => {
        return hotels.filter(hotel => {
            const priceRange = hotel.price.match(/\d+/g);
            if (!priceRange || priceRange.length < 2) {
                // If the format isn't as expected or we can't get two numbers, skip this entry
                return false;
            }

            // Parse the prices into numbers
            const minHotelPrice = parseFloat(priceRange[0]);
            const maxHotelPrice = parseFloat(priceRange[1]);

            // User input filters, with fallbacks
            const minPrice = parseFloat(priceFilter.min) || 0;
            const maxPrice = parseFloat(priceFilter.max) || Infinity;

            // Check if the hotel's price range overlaps with the user's specified range
            return (maxHotelPrice >= minPrice && minHotelPrice <= maxPrice);
        });
    };

    return (
        <>
            <Navbar/>
            <div className="map-page">
                <div className="sidebar">
                    <h4>Filter Restaurants by Cuisine</h4>
                    <select
                        value={selectedCuisine}
                        onChange={e => setSelectedCuisine(e.target.value)}
                        className="dropdown">
                        <option value="">Select Cuisine</option>
                        {allCuisines.map(cuisine => (
                            <option key={cuisine} value={cuisine}>{cuisine}</option>
                        ))}
                    </select>
                    <br></br>
                    <br></br>
                    <h4>Filter Hotels by Price Range</h4>
                    <input type="number" placeholder="Min price" value={priceFilter.min}
                           onChange={e => setPriceFilter(prev => ({...prev, min: e.target.value}))}/>
                    <input type="number" placeholder="Max price" value={priceFilter.max}
                           onChange={e => setPriceFilter(prev => ({...prev, max: e.target.value}))}/>
                </div>
                <div className="map-section">

                    {attractions.length > 0 ? (
                        <MapContainer center={mapCenter} zoom={12.5} scrollWheelZoom={true} className="map-container">
                            <TileLayer
                                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            />
                            {attractions.map((attraction, index) => (
                                <Marker key={index}
                                        position={[attraction.latitude, attraction.longitude]}
                                        icon={orchidPin}>
                                    <Popup className="popup">
                                        <b>{attraction.name}</b><br/>
                                        Rating: {attraction.rating}<br/>
                                        <a href={attraction.url} target="_blank" rel="noopener noreferrer">More info</a>
                                    </Popup>
                                </Marker>
                            ))}
                            {filterRestaurants().map((restaurant, index) => (
                                <Marker key={index}
                                        position={[restaurant.latitude, restaurant.longitude]}
                                        icon={blueGreenPin}>
                                    <Popup className="popup">
                                        <b>{restaurant.name}</b><br/>
                                        Rating: {restaurant.rating}<br/>
                                        Phone: {restaurant.phone}<br/>
                                        Email: {restaurant.email}<br/>
                                        Price Level: {restaurant.price_level}<br/>
                                        Cuisine: {restaurant.cuisine.map(c => c.name).join(", ")}<br/>
                                        <a href={restaurant.url} target="_blank" rel="noopener noreferrer">More info</a>
                                    </Popup>
                                </Marker>
                            ))}
                            {filterHotels().map((hotel, index) => (
                                <Marker key={index}
                                        position={[hotel.latitude, hotel.longitude]}
                                        icon={redPin}>
                                    <Popup className="popup">
                                        <b>{hotel.name}</b><br/>
                                        Rating: {hotel.rating}<br/>
                                        Price Level: {hotel.price_level}<br/>
                                        Price: {hotel.price}<br/>
                                        <a href={hotel.url} target="_blank" rel="noopener noreferrer">More info</a>
                                    </Popup>
                                </Marker>
                            ))}
                        </MapContainer>


                    ) : (
                        <p>Loading map...</p>
                    )}
                    <br></br>
                    <div className="legend">
                        <div className="legend-item">
                            <div className="legend-color attractions"></div>
                            <div>Attractions</div>
                        </div>
                        <div className="legend-item">
                            <div className="legend-color restaurants"></div>
                            <div>Restaurants</div>
                        </div>
                        <div className="legend-item">
                            <div className="legend-color hotels"></div>
                            <div>Hotels</div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default MapPage;
