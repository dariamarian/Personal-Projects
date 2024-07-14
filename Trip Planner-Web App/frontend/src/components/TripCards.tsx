import {useEffect, useState} from "react";
import {Trip, User} from "@/utils/types.tsx";
import {UserService} from "@/apis/profile/UserService.tsx";
import {TripService} from "@/apis/trip/TripService.tsx";
import TripCard from "@/components/TripCard.tsx";

const TripCards = () => {
    const [trips, setTrips] = useState<Trip[]>([]);
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

    const loadTrips = async () => {
        if (!currentUser || !currentUser.username || !currentUser.password || !currentUser.id) {
            return;
        }

        const userTrips = await TripService.getTrips(Number(currentUser.id));

        setTrips(userTrips);
    };

    useEffect(() => {
        if (currentUser && currentUser.username && currentUser.password && currentUser.id){
            loadTrips();
        }
    }, [currentUser]);

    useEffect(() => {
        if (currentUser) {
            loadFavoriteTrips();
        }
    }, [currentUser]);

    const loadFavoriteTrips = async () => {
        if (currentUser?.username && currentUser.password && currentUser.id) {
            const favoriteTrips = await TripService.getFavoriteTrips(
                Number(currentUser.id),
            );
            setFavorites(favoriteTrips);
        }
    };
    const updateFavorites = (trip: Trip, isFavorite: boolean) => {
        if (isFavorite) {
            setFavorites((prevFavorites) => [...prevFavorites, trip]);
        } else {
            setFavorites((prevFavorites) =>
                prevFavorites.filter((favorite) => favorite.trip_id !== trip.trip_id),
            );
        }
    };

    return (
        <div className="flex flex-wrap justify-center px-2 pt-5">
            {trips.map((trip, index) => (
                <div key={index} className="m-2">
                    <TripCard
                        trip={trip}
                        isFavorite={favorites.some((favorite) => favorite.trip_id === trip.trip_id)}
                        currentUser={currentUser!}
                        onFavoriteChange={() =>
                            updateFavorites(
                                trip,
                                !favorites.some((favorite) => favorite.trip_id === trip.trip_id),
                            )
                        }
                    />
                </div>
            ))}
        </div>
    );
}

export default TripCards;
