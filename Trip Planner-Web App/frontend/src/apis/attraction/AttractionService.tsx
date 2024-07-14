import {handleGetAttractions, handleParseAttractions, handleSortAttractions} from "@/apis/attraction/attractionAPI";

const parseAttractions = (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleParseAttractions(username!, password!, locationId)
};

const getAttractions = async (locationId: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return await handleGetAttractions(username!, password!, locationId);
};

const sortAttractions = (locationId: string, strategy: string) => {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    return handleSortAttractions(username!, password!, locationId, strategy)
        .then((response) => {
            return response;
        })

}

export const AttractionService = {
    parseAttractions,
    getAttractions,
    sortAttractions
}