import {handleGetCity} from "@/apis/city/cityAPI";

const getCity = (inputValue: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetCity(username!, password!, inputValue)
        .then((response) => {
            return response.data;
        })

};
export const CityService = {
    getCity,
}