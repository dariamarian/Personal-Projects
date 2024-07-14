import axios from "axios";
import {restaurantsUrl, locationsUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";

export const handleParseRestaurants = (username: string, password: string, locationId: string) => {
    return axios.get(`${restaurantsUrl}?locationId=${locationId}`, secureConfig(username, password))
}

export const handleGetRestaurants = (username: string, password: string, locationId: string) => {
    return axios.get(`${locationsUrl}/${locationId}/restaurants`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting restaurants:', error);
            throw error;
        });
}

export const handleSortRestaurants = (username: string, password: string, locationId: string, strategy: string) => {
    return axios.get(`${locationsUrl}/${locationId}/restaurants/sort?strategy=${strategy}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error sorting restaurants:', error);
            throw error;
        });
}