export type CategoryItem = {
    name: string;
    items: string[];
};

export type Categories = CategoryItem[];

export type User = {
    id: string;
    username: string;
    name: string;
    email: string;
    password: string;
    gender: Gender;
    description: string;
};

export type UserRequest = {
    name: string;
    email: string;
    gender: Gender;
    description: string;
}

export type LoginRequest = {
    username: string;
    password: string;
}

export enum Gender { MALE = "MALE", FEMALE = "FEMALE", OTHER = "OTHER" }


export type Location = {
    name: string;
    location_id: number;
    latitude: number;
    longitude: number;
}

export type City = {
    latitude: number;
    longitude: number;
    name: string;
    country: string;
}

export type Attraction = {
    attraction_id: number,
    locationId: string;
    name: string;
    rating: number;
    url: string;
    latitude: number;
    longitude: number;
}

export type Restaurant = {
    restaurant_id: number,
    locationId: string;
    name: string;
    rating: number;
    phone: string;
    email: string;
    url: string;
    price_level: string;
    cuisine: Cuisine[];
    latitude: number;
    longitude: number;
}

export type Hotel = {
    hotel_id: number,
    locationId: string;
    name: string;
    rating: number;
    url: string;
    price_level: string;
    price: string;
    latitude: number;
    longitude: number;
}

export type Cuisine = {
    cuisine_id: number;
    name: string;
    restaurant_id: number;
}

export type TripRequest = {
    city: string;
    startDate: string;
    endDate: string;
    userId: string;
    attractions: Attraction[];
    restaurants: Restaurant[];
    hotels: Hotel[];
}

export type Trip = {
    trip_id: number;
    city: string;
    startDate: string;
    endDate: string;
    userId: string;
    attractions: Attraction[];
    restaurants: Restaurant[];
    hotels: Hotel[];
}

export interface FavoriteTrip {
    id?: number;
    idTrip: number;
    idUser: number;
}

export interface Suggestion {
    city: string;
    startDate: string;
    endDate: string;
}


export enum COLORS {
    BLUE_GREEN = "#3CACAE",
    WHITE = "#FFFFFF",
    BLACK = "#000000",
    ORCHID = "#C26DBC"
}