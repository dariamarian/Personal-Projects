import {weatherUrl} from "@/apis/urlConstants";
import {secureConfig} from "@/apis/config/apiConfigs";
import axios from "axios";

export const handleGetWeather = (username: string, password: string, latitude: number, longitude: number) => {
    return axios.get(`${weatherUrl}?lat=${latitude}&lon=${longitude}`, secureConfig(username, password));
}