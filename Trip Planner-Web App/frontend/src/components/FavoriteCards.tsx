import { UserService } from "@/apis/profile/UserService";
import {TripService} from "@/apis/trip/TripService.tsx";
import { useState, useEffect } from "react";
import {Trip, User} from "@/utils/types";
import TripCard from "@/components/TripCard.tsx";


const FavoriteCards = () => {
    const [currentUser, setCurrentUser] = useState<User>();
    const [favorites, setFavorites] = useState<Trip[]>([]);

    useEffect(() => {
        loadUserInfo();
    }, []);

    const loadUserInfo = async () => {
        await UserService.getCurrentUser()
            .then((user) => {
                setCurrentUser(user);
            });
    };

    const fetchFavoriteTrips = async () => {
        const favoriteTrips = await TripService.getFavoriteTrips(Number(currentUser!.id));
        setFavorites(favoriteTrips);
    };

    useEffect(() => {
        if (currentUser && currentUser.username && currentUser.password && currentUser.id) {
            fetchFavoriteTrips();
        }
    }, [currentUser]);

    const handleFavoriteChange = () => {
        fetchFavoriteTrips();
    };

    return (
        <div className="flex flex-wrap justify-center px-2 pt-5">
            {favorites.map((trip, index) => (
                <div key={index} className="m-2">
                    <TripCard
                        trip={trip}
                        isFavorite={true}
                        currentUser={currentUser!}
                        onFavoriteChange={handleFavoriteChange}
                    />
                </div>
            ))}
        </div>
    );
};

export default FavoriteCards;
