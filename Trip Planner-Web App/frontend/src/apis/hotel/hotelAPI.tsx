import axios from "axios";
import {hotelsUrl, locationsUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";

export const handleParseHotels = (username: string, password: string, locationId: string) => {
    return axios.get(`${hotelsUrl}?locationId=${locationId}`, secureConfig(username, password))
}

export const handleGetHotels = (username: string, password: string, locationId: string) => {
    return axios.get(`${locationsUrl}/${locationId}/hotels`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error getting hotels:', error);
            throw error;
        });
}

export const handleSortHotels = (username: string, password: string, locationId: string, strategy: string) => {
    return axios.get(`${locationsUrl}/${locationId}/hotels/sort?strategy=${strategy}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error sorting hotels:', error);
            throw error;
        });
}