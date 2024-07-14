import {cityUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";
import axios from "axios";

export const handleGetCity = (username: string, password: string, prefix: string) => {
    return axios.get(`${cityUrl}?prefix=${prefix}`, secureConfig(username, password))
        .then(response => {
            return response.data;
        })
        .catch(error => {
            console.error('Error fetching cities:', error);
            if (error.response) {
                console.error('Error status:', error.response.status);
                console.error('Error data:', error.response.data);
            }
            throw error;
        });
};
