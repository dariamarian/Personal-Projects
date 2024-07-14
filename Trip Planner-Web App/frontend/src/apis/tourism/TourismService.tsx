import {handleParseEntities} from "@/apis/tourism/tourismAPI.tsx";

const parseEntities = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleParseEntities(username!, password!, locationId)
};

export const TourismService = {
    parseEntities
}