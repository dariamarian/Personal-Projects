import {
    handleAddTrip,
    handleAddTripToFavorites,
    handleDeleteTrip,
    handleGetFavoriteTrips,
    handleGetTrip,
    handleGetTrips,
    handleGetUserSuggestions,
    handleRemoveTripFromFavorites
} from "@/apis/trip/tripAPI";
import {FavoriteTrip, TripRequest} from "@/utils/types";
import {tripAddFail, tripAddSuccess} from "@/apis/auth/responseConstants.tsx";

const getTrips = (userId: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetTrips(username!, password!, userId)
        .then((response) => {
            return response;
        })

};

const addTrip = (trip: TripRequest) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleAddTrip(username!, password!, trip)
        .then((response) => {
            return {status: response.status, message: tripAddSuccess};
        })
        .catch((err) => {
            return {status: err.status, message: tripAddFail};
        });

}

const deleteTrip = (tripId: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleDeleteTrip(username!, password!, tripId)
        .then((response) => {
            return response;
        })

}

const getTrip = (tripId: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetTrip(username!, password!, tripId)
        .then((response) => {
            return response;
        })

}

const addTripToFavorites = (favoriteTrip: FavoriteTrip) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleAddTripToFavorites(username!, password!, favoriteTrip)
        .then((response) => {
            return response;
        })
}

const removeTripFromFavorites = (favoriteTrip: FavoriteTrip) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleRemoveTripFromFavorites(username!, password!, favoriteTrip)
        .then((response) => {
            return response;
        })
}

const getFavoriteTrips = (userId: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetFavoriteTrips(username!, password!, userId)
        .then((response) => {
            return response;
        })
}

const getUserSuggestions = (userId: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetUserSuggestions(username!, password!, userId)
        .then((response) => {
            return response;
        })

}

export const TripService = {
    getTrips,
    addTrip,
    deleteTrip,
    getTrip,
    addTripToFavorites,
    removeTripFromFavorites,
    getFavoriteTrips,
    getUserSuggestions
}