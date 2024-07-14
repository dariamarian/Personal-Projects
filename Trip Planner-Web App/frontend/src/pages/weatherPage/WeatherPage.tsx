import {useEffect, useState} from "react";
import {WeatherService} from "@/apis/weather/WeatherService";
import "./WeatherPage.css";
import {useParams} from "react-router-dom";
import {LocationService} from "@/apis/location/LocationService";
import {Location} from "@/utils/types";
import Navbar from "@/components/Navbar.tsx";

const WeatherPage = () => {
    const {locationName} = useParams(); // Retrieve locationName from URL parameters
    const [location, setLocation] = useState<Location | null>(null);
    const [data, setData] = useState<WeatherData | null>(null);

    useEffect(() => {
        if (locationName) {
            const fetchLocation = async () => {
                try {
                    const response = await LocationService.getLocationByName(locationName);
                    setLocation(response);
                } catch (error) {
                    console.error("Error fetching location:", error);
                }
            };
            fetchLocation();
        }
    }, [locationName]);


    useEffect(() => {
        const fetchWeatherData = async () => {
            if (location && location.latitude && location.longitude) {
                try {
                    const weatherData = await WeatherService.getWeather(location.latitude, location.longitude);
                    setData(weatherData);
                } catch (error) {
                    console.error("Error fetching weather data:", error);
                }
            }
        };

        fetchWeatherData();
    }, [location]); // Fetch weather data whenever location changes


    return (
        <>
            <Navbar/>
            <div className="weather-page">
                {data && (
                    <div className="current-weather">
                        <div className="top">
                            <div>
                                <p className="current-city"> {locationName} </p>
                                <p className="current-description">{data.current.weather[0].description}</p>
                            </div>
                            <img alt="weather" className="current-icon"
                                 src={`/icons/${data.current.weather[0].icon}.png`}/>
                        </div>
                        <div className="bottom">
                            <p className="current-temp">{Math.round(data.current.temp)}°C</p>
                            <div className="details">
                                <div className="row">
                                    <span className="label">feels like: </span>
                                    <span className="value">{data.current.feels_like}°C</span>
                                </div>
                                <div className="row">
                                    <span className="label">wind speed: </span>
                                    <span className="value">{data.current.wind_speed} m/s</span>
                                </div>
                                <div className="row">
                                    <span className="label">humidity: </span>
                                    <span className="value">{data.current.humidity}%</span>
                                </div>
                                <div className="row">
                                    <span className="label">UV index: </span>
                                    <span className="value">{data.current.uvi}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
                {data && (
                    <div className="hourly-forecast">
                        <div style={{display: "flex", flexWrap: "wrap"}}>
                            {data.hourly.slice(1, 25).map((hour, index) => (
                                <div key={index} style={{margin: "0 10px", display: "flex", flexDirection: "column"}}>
                                <span
                                    className="hourly-hour">{new Date(hour.dt * 1000).toLocaleTimeString("en-US", {hour: "numeric"})}</span>
                                    <span className="hourly-temp">{Math.round(hour.temp)}°C</span>
                                    <img alt="weather" className="hourly-icon"
                                         src={`/icons/${hour.weather[0].icon}.png`}/>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
                {data && (
                    <div className="daily-forecast">
                        {data.daily.slice(1, 8).map((day, index) => (
                            <div key={index} className="daily-item">
                                <img alt="weather" className="daily-icon" src={`/icons/${day.weather[0].icon}.png`}/>
                                <label
                                    className="daily-day">{new Date(day.dt * 1000).toLocaleDateString("en-US", {weekday: "long"})}</label>
                                <label className="daily-description">{day.weather[0].description}</label>
                                <label
                                    className="daily-temp">{Math.round(day.temp.min)}°C/{Math.round(day.temp.max)}°C</label>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}

export default WeatherPage;

interface WeatherData {
    lat: number;
    lon: number;
    timezone: string;
    current: {
        dt: number;
        temp: number;
        feels_like: number;
        humidity: number;
        pressure: number;
        weather: {
            main: string;
            description: string;
            icon: string;
        }[];
        wind_speed: number;
        visibility: number;
        uvi: number;
    };
    hourly: {
        dt: number;
        temp: number;
        weather: {
            main: string;
            description: string;
            icon: string;
        }[];
    }[];
    daily: {
        dt: number;
        temp: {
            day: number;
            min: number;
            max: number;
        };
        weather: {
            main: string;
            description: string;
            icon: string;
        }[];
    }[];
}