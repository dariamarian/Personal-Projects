import axios from "axios";
import {attractionsUrl, locationsUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";

export const handleParseAttractions = (username: string, password: string, locationId: string) => {
    return axios.get(`${attractionsUrl}?locationId=${locationId}`, secureConfig(username, password))
}

export const handleGetAttractions = (username: string, password: string, locationId: string) => {
    return axios.get(`${locationsUrl}/${locationId}/attractions`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting attractions:', error);
            throw error;
        });
}

export const handleSortAttractions = (username: string, password: string, locationId: string, strategy: string) => {
    return axios.get(`${locationsUrl}/${locationId}/attractions/sort?strategy=${strategy}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error sorting attractions:', error);
            throw error;
        });
}