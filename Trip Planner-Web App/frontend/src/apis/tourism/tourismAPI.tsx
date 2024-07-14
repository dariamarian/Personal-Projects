import axios from "axios";
import {entitiesUrl} from "@/apis/urlConstants.tsx";
import {secureConfig} from "@/apis/config/apiConfigs.tsx";

export const handleParseEntities = (username: string, password: string, locationId: string) => {
    return axios.get(`${entitiesUrl}/parse?locationId=${locationId}`, secureConfig(username, password))
}
