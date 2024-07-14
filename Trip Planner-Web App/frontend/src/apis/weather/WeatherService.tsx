import {handleGetWeather} from "@/apis/weather/weatherAPI";

const getWeather = (latitude: number, longitude: number) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");
    return handleGetWeather(username!, password!, latitude, longitude)
        .then((response) => {
            return response.data;
        })
};

export const WeatherService = {
    getWeather,
}