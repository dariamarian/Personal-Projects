import axios from "axios";
import {usersUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";
import {FavoriteTrip, TripRequest} from "@/utils/types";

export const handleGetTrips = (username: string, password: string, userId: number) => {
    return axios.get(`${usersUrl}/${userId}/trips`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting trips:', error);
            throw error;
        });
}

export const handleAddTrip = (username: string, password: string, trip: TripRequest) => {
    return axios.post(`${usersUrl}/trips`, trip, secureConfig(username, password));
}

export const handleDeleteTrip = (username: string, password: string, tripId: number) => {
    return axios.delete(`${usersUrl}/trips/${tripId}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error deleting trip:', error);
            throw error;
        });
}

export const handleGetTrip = (username: string, password: string, tripId: number) => {
    return axios.get(`${usersUrl}/trips/${tripId}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting trip:', error);
            throw error;
        });
}

export const handleAddTripToFavorites = (username: string, password: string, favoriteTrip: FavoriteTrip) => {
    return axios.post(`${usersUrl}/trips/favorites`, favoriteTrip, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error adding trip to favorites:', error);
            throw error;
        });
}

export const handleRemoveTripFromFavorites = (username: string, password: string, favoriteTrip: FavoriteTrip) => {
    return axios.delete(`${usersUrl}/trips/favorites`, {
        headers: {
            'Content-Type': 'application/json',
        },
        data: favoriteTrip,
        auth: {
            username: username,
            password: password
        }
    }).then(response => {
        return response.data;
    })
        .catch(error => {
            console.error('Error removing trip from favorites:', error);
            throw error;
        });
}

export const handleGetFavoriteTrips = (username: string, password: string, userId: number) => {
    return axios.get(`${usersUrl}/${userId}/trips/favorites`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting favorite trips:', error);
            throw error;
        });
}

export const handleGetUserSuggestions = (username: string, password: string, userId: number) => {
    return axios.get(`${usersUrl}/${userId}/suggestions`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting user suggestions:', error);
            throw error;
        });
}