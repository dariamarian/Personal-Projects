import React from "react";
import {Card, CardContent, CardTitle} from "./ui/card";
import {FavoriteTrip, Trip, User} from "@/utils/types.tsx";
import {FaHotel, FaLandmark, FaUtensils} from "react-icons/fa";
import {format} from "date-fns";
import {TripService} from "@/apis/trip/TripService";
import {Button} from "./ui/button";
import {LucideHeart} from "lucide-react";

interface TripCardProps {
    trip: Trip;
    isFavorite?: boolean;
    currentUser?: User;
    onFavoriteChange?: () => void;
}

const TripCard: React.FC<TripCardProps> = ({
                                               trip,
                                               isFavorite,
                                               currentUser,
                                               onFavoriteChange
                                           }) => {
    const handleFavoriteClick = async () => {
        const favoriteTripData: FavoriteTrip = {
            idTrip: trip.trip_id,
            idUser: Number(currentUser?.id),
        };

        try {
            if (isFavorite) {
                // If it's already a favorite, remove it
                await TripService.removeTripFromFavorites(
                    favoriteTripData,
                );
                console.log("Removed from favorites" + favoriteTripData);
            } else {
                // If it's not a favorite, add it
                await TripService.addTripToFavorites(
                    favoriteTripData,
                );
            }
            if (onFavoriteChange) {
                onFavoriteChange();
            }
            // Toggle the favorite status
        } catch (error) {
            // Handle errors
            console.error("Error toggling favorite status", error);
        }
    };
    const formatDate = (dateString: string) => format(new Date(dateString), "dd.MM.yyyy");

    return (
        <>
            <style>
                {`
          .custom-scrollbar::-webkit-scrollbar {
            width: 5px;
          }
          
          .custom-scrollbar::-webkit-scrollbar-track {
            background: #f0f0f0;
            border-radius: 10px;
          }
          
          .custom-scrollbar::-webkit-scrollbar-thumb {
            background: #EEB5EB; 
            border-radius: 10px;
          }
          
          .custom-scrollbar::-webkit-scrollbar-thumb:hover {
            background: #C26DBC;
          }
        `}
            </style>
            <Card
                className="h-[380px] w-[300px] bg-white shadow-xl rounded-lg overflow-hidden border-2 border-gray-200">
                <div
                    className="p-4 bg-gradient-to-r from-pink-300 to-teal-300 rounded-t-lg flex justify-between items-center">
                    <div>
                        <CardTitle className="text-2xl font-semibold text-white">
                            {trip.city}
                        </CardTitle>
                        <span
                            className="text-sm font-normal text-white block"> {formatDate(trip.startDate)} - {formatDate(trip.endDate)}</span>
                    </div>
                    <Button
                        onClick={handleFavoriteClick}
                        style={{background: "transparent", display: "flex", alignItems: "center"}}
                    >
                        {isFavorite ? (
                            <LucideHeart size={20} className="heartAnimation"
                                         style={{color: "#C26DBC", fill: "#C26DBC"}}/>
                        ) : (
                            <LucideHeart size={20} style={{color: "black"}}/>
                        )}
                    </Button>
                </div>
                <CardContent
                    className="px-4 py-2 bg-gradient-to-b from-cyan-100 to-pink-100 overflow-y-auto max-h-[calc(380px-100px)] custom-scrollbar">
                    <div className="space-y-4">
                        {trip.attractions.length > 0 && (
                            <div>
                                <p className="flex items-center text-lg font-semibold mb-1 text-[#3CACAE]">
                                    <FaLandmark
                                        className="mr-2 text-[#3CACAE]"/>Attractions:</p>
                                <ul className="list-disc pl-5 text-gray-700">
                                    {trip.attractions.map((attraction) => (
                                        <li key={attraction.attraction_id}
                                            className="text-sm hover:text-[#3CACAE] cursor-pointer transition-colors">
                                            <a href={attraction.url} target="_blank"
                                               rel="noopener noreferrer">{attraction.name}</a>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        {trip.restaurants.length > 0 && (
                            <div>
                                <p className="flex items-center text-lg font-semibold mb-1 text-[#C26DBC]">
                                    <FaUtensils
                                        className="mr-2 text-[#C26DBC]"/>Restaurants:</p>
                                <ul className="list-disc pl-5 text-gray-700">
                                    {trip.restaurants.map((restaurant) => (
                                        <li key={restaurant.restaurant_id}
                                            className="text-sm hover:text-[#C26DBC] cursor-pointer transition-colors">
                                            <a href={restaurant.url} target="_blank"
                                               rel="noopener noreferrer">{restaurant.name}</a>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        {trip.hotels.length > 0 && (
                            <div>
                                <p className="flex items-center text-lg font-semibold mb-1 text-[#3CACAE]"><FaHotel
                                    className="mr-2 text-[#3CACAE]"/>Hotels:</p>
                                <ul className="list-disc pl-5 text-gray-700">
                                    {trip.hotels.map((hotel) => (
                                        <li key={hotel.hotel_id}
                                            className="text-sm hover:text-[#3CACAE] cursor-pointer transition-colors">
                                            <a href={hotel.url} target="_blank"
                                               rel="noopener noreferrer">{hotel.name}</a>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                </CardContent>
            </Card>
        </>
    );
};

export default TripCard;
