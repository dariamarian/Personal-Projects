import axios from "axios";
import {locationsUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";

export const handleGetLocationByName = (username: string, password: string, name: string) => {
    return axios.get(`${locationsUrl}/name/${name}`, secureConfig(username, password))
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else if (response.status === 404) {
                return null; // Location not found
            } else {
                throw new Error("Failed to get location");
            }
        })
        .catch((error) => {
            console.error("Error fetching location:", error);
            throw error;
        });
}

export const handleGetLocationById = (username: string, password: string, locationId: string) => {
    return axios.get(`${locationsUrl}/id/${locationId}`, secureConfig(username, password))
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else if (response.status === 404) {
                return null; // Location not found
            } else {
                throw new Error("Failed to get location");
            }
        })
        .catch((error) => {
            console.error("Error fetching location:", error);
            throw error;
        });
}

export const handleParseLocation = (username: string, password: string, name: string) => {
    return axios.get(`${locationsUrl}/location?location=${name}`, secureConfig(username, password));
}