import {handleGetHotels, handleParseHotels, handleSortHotels} from "@/apis/hotel/hotelAPI";

const parseHotels = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleParseHotels(username!, password!, locationId)

};

const getHotels = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetHotels(username!, password!, locationId)
        .then((response) => {
            return response;
        })

};

const sortHotels = (locationId: string, strategy: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleSortHotels(username!, password!, locationId, strategy)
        .then((response) => {
            return response;
        })

}

export const HotelService = {
    parseHotels,
    getHotels,
    sortHotels
}