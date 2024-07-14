import {Location} from "@/utils/types";
import {
    handleGetLocationByName,
    handleGetLocationById,
    handleParseLocation
} from "@/apis/location/locationAPI";

const getLocationByName = (name: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleGetLocationByName(username!, password!, name)
        .then((location) => {
            return location;
        })
        .catch((error) => {
            if (error.response && error.response.status === 404) {
                // Location not found
                return null;
            } else {
                // Other errors
                throw error;
            }
        }) as Promise<Location | null>;
};

const getLocationById = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleGetLocationById(username!, password!, locationId)
        .then((location) => {
            return location;
        })
        .catch((error) => {
            console.error("Error fetching location:", error);
            if (error.response && error.response.status === 404) {
                return null;
            } else {
                throw error;
            }
        }) as Promise<Location | null>;
};

const parseLocation = (name: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleParseLocation(username!, password!, name)
        .then((response) => {
            return response.data;
        });
}

export const LocationService = {
    getLocationByName,
    getLocationById,
    parseLocation
}