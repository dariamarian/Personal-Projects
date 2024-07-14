import {handleGetRestaurants, handleParseRestaurants, handleSortRestaurants} from "@/apis/restaurant/restaurantAPI";

const parseRestaurants = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleParseRestaurants(username!, password!, locationId)

};

const getRestaurants = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleGetRestaurants(username!, password!, locationId)
        .then((response) => {
            return response;
        })

};

const sortRestaurants = (locationId: string, strategy: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleSortRestaurants(username!, password!, locationId, strategy)
        .then((response) => {
            return response;
        })

}

export const RestaurantService = {
    parseRestaurants,
    getRestaurants,
    sortRestaurants
}