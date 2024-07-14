import {useEffect, useState} from "react";
import Search from "@/components/Search.tsx";
import {
    Attraction,
    City,
    COLORS,
    Hotel,
    Location,
    Restaurant,
    Suggestion,
    Trip,
    TripRequest,
    User
} from "@/utils/types";
import {LocationService} from "@/apis/location/LocationService";
import AttractionsDropdown from "@/components/AttractionsDropdown";
import RestaurantsDropdown from "@/components/RestaurantsDropdown.tsx";
import HotelsDropdown from "@/components/HotelsDropdown.tsx";
import {TripService} from "@/apis/trip/TripService";
import {UserService} from "@/apis/profile/UserService";
import {Tooltip, TooltipContent, TooltipProvider, TooltipTrigger} from "@/components/ui/tooltip";
import { useNavigate } from 'react-router-dom';
import {Button} from "@/components/ui/button";
import {CalendarIcon, MapIcon, SunIcon} from "lucide-react";
import Navbar from "@/components/Navbar.tsx";
import "./MainPage.css"
import {Calendar} from "@/components/ui/calendar.tsx";
import {Popover, PopoverContent, PopoverTrigger,} from "@/components/ui/popover";
import {cn} from "@/lib/utils.ts";
import Suggestions from "@/components/Suggestions";
import {toast} from "@/components/ui/use-toast.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import {AttractionService} from "@/apis/attraction/AttractionService.tsx";
import {RestaurantService} from "@/apis/restaurant/RestaurantService.tsx";
import {HotelService} from "@/apis/hotel/HotelService.tsx";
import {TourismService} from "@/apis/tourism/TourismService.tsx";

const styles = {
    mapButton: {
        backgroundColor: COLORS.ORCHID,
        color: COLORS.WHITE,
    },
    addButton: {
        backgroundColor: COLORS.ORCHID,
        color: COLORS.WHITE,
    },
    weatherButton: {
        backgroundColor: COLORS.ORCHID,
        color: COLORS.WHITE,
    },
};

const MainPage = () => {
    const [selectedOption, setSelectedOption] = useState<City | null>(null);
    const [location, setLocation] = useState<Location | null>(null);
    const [startDate, setStartDate] = useState<Date | undefined>();
    const [endDate, setEndDate] = useState<Date | undefined>();
    const [currentUser, setCurrentUser] = useState<User>();
    const [attractions, setAttractions] = useState<Attraction[]>([]);
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [hotels, setHotels] = useState<Hotel[]>([]);
    const [sortAttractionsBy, setSortAttractionsBy] = useState('');
    const [sortRestaurantsBy, setSortRestaurantsBy] = useState('');
    const [sortHotelsBy, setSortHotelsBy] = useState('');
    const navigate = useNavigate();
    const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
    const [dataReady, setDataReady] = useState(false);

    useEffect(() => {
        const storedCity = localStorage.getItem('selectedCity');
        if (storedCity) {
            setSelectedOption(JSON.parse(storedCity));
            handleSelect(JSON.parse(storedCity));
        }
    }, []);


    const onMapClick = () => {
        navigate("/map/" + location?.location_id);
    };

    const onWeatherClick = () => {
        navigate("/weather/" + location?.name);
    };

    const handleAttractionsChange = (selectedOptions: Attraction[]) => {
        setAttractions(selectedOptions);
    };

    const handleRestaurantsChange = (selectedOptions: Restaurant[]) => {
        setRestaurants(selectedOptions);
    };

    const handleHotelsChange = (selectedOptions: Hotel[]) => {
        setHotels(selectedOptions);
    };

    useEffect(() => {
        document.body.classList.add('main-page');

        return () => {
            document.body.classList.remove('main-page');
        };
    }, []);

    useEffect(() => {
        loadUserInfo();
    }, []);

    useEffect(() => {
        if (currentUser) {
            fetchSuggestions(parseFloat(currentUser.id));
        }
    }, [currentUser]);

    const loadUserInfo = async () => {
        await UserService.getCurrentUser().then((user) => {
            setCurrentUser(user);
        });
    };

    const fetchSuggestions = async (userId: number) => {
        const tripSuggestions = await TripService.getUserSuggestions(userId);
        // map to suggestions
        const suggestions = tripSuggestions.map((trip: Trip) => {
            return {
                city: trip.city,
                startDate: trip.startDate,
                endDate: trip.endDate,
            };
        });
        setSuggestions(suggestions);
    };

    const handleSelect = async (option: City) => {
        // Reset the state when a new city is selected
        setDataReady(false);
        setStartDate(undefined);
        setEndDate(undefined);
        setAttractions([]);
        setRestaurants([]);
        setHotels([]);
        localStorage.setItem('selectedCity', JSON.stringify(option));
        if (!option.name) return;
        try {
            const locationName = option.name.split(",")[0].trim();

            let location = await LocationService.getLocationByName(locationName);
            if (location) {
                setLocation(location);
            } else {
                const encodedLocationName = encodeURIComponent(locationName);
                await LocationService.parseLocation(encodedLocationName);
                location = await LocationService.getLocationByName(locationName);
                setLocation(location);
            }
            if (location) {
                await fetchAllEntities(location.location_id);
            }
        } catch
            (error) {
            console.error("Error checking or adding location:", error);
        }
        if (selectedOption?.name !== option.name) {
            setSelectedOption(option);
        }
    };

    const fetchAllEntities = async (locationId: number) => {
        try {
            let attractionsData = await AttractionService.getAttractions(locationId.toString());
            let restaurantsData = await RestaurantService.getRestaurants(locationId.toString());
            let hotelsData = await HotelService.getHotels(locationId.toString());
            if (attractionsData.length === 0 && restaurantsData.length === 0 && hotelsData.length === 0) {
                await TourismService.parseEntities(locationId.toString());
                attractionsData = await AttractionService.getAttractions(locationId.toString());
                restaurantsData = await RestaurantService.getRestaurants(locationId.toString());
                hotelsData = await HotelService.getHotels(locationId.toString());
            }
            setAttractions(attractionsData);
            setRestaurants(restaurantsData);
            setHotels(hotelsData);
            setDataReady(true);
        } catch (error) {
            console.error("Error fetching entities data:", error);
        }
    };

    function validateInput() {
        let err = "";
        let c = 0;
        if (startDate == null) {
            err = err + "Start date must be selected. ";
            c++;
        }
        if (endDate == null) {
            err = err + "End date must be selected. ";
            c++;
        }
        if (startDate != null && endDate != null && startDate > endDate) {
            err = err + "Start date must be before end date. ";
            c++;

        }
        if (err.length > 0)
            showErrorToast(err);
        return c == 0;
    }

    const showErrorToast = (error: string) => {
        console.log("should show");
        toast({
            title: "Error",
            variant: "destructive",
            description: error,
        })
    }

    const onAddTrip = async () => {
        if (!validateInput()) {
            return;
        }

        if (selectedOption && startDate && endDate && currentUser) {
            const trip: TripRequest = {
                city: selectedOption.name,
                startDate: startDate.toISOString(),
                endDate: endDate.toISOString(),
                userId: currentUser.id,
                attractions: attractions, // Add selected attractions here
                restaurants: restaurants, // Add selected restaurants here
                hotels: hotels, // Add selected hotels here
            };
            try {
                const {status, message} = await TripService.addTrip(trip);
                toast({
                    title: status === 200 ? "Success" : "Error",
                    variant: status === 200 ? "default" : "destructive",
                    description: message,
                })
                // Optionally reset the state after adding the trip
                setStartDate(undefined);
                setEndDate(undefined);
                setSelectedOption(null);
                setLocation(null);
            } catch (error) {
                console.error("Error adding trip:", error);
            }
        }
    };

    return (
        <>
            <Navbar/>
            <div className="main-container">
                <div className="plan-div">
                    <h1 className="plan-a-trip">Plan a trip</h1>
                </div>
                <div className="plan-div select-container">
                    <Search onSelect={handleSelect} selectedCity={selectedOption}/>
                </div>
                <div className="flex justify-center gap-7 button-group">
                    <TooltipProvider>
                        <Tooltip>
                            <TooltipTrigger>
                                <Button onClick={onMapClick} style={styles.mapButton}>
                                    <MapIcon className="h-4 w-4"/>
                                    <span className="font-sm ml-2">Map</span>
                                </Button>
                            </TooltipTrigger>
                            <TooltipContent>
                                <span className="text-sm">See on map</span>
                            </TooltipContent>
                        </Tooltip>
                    </TooltipProvider>
                    <TooltipProvider>
                        <Tooltip>
                            <TooltipTrigger>
                                <Button onClick={onWeatherClick} style={styles.weatherButton}>
                                    <SunIcon className="h-4 w-4"/>
                                    <span className="font-sm ml-2">Weather</span>
                                </Button>
                            </TooltipTrigger>
                            <TooltipContent>
                                <span className="text-sm">Check the weather</span>
                            </TooltipContent>
                        </Tooltip>
                    </TooltipProvider>
                </div>
                <div className="trip">
                    {location && dataReady && (
                        <div className="select-attractions-restaurants-hotels">
                            <AttractionsDropdown
                                parsedAttractions={attractions}
                                locationId={location.location_id.toString()}
                                onAttractionsChange={handleAttractionsChange}
                                sortCriteria={sortAttractionsBy}
                                updateSortCriteria={setSortAttractionsBy}
                            />
                            <RestaurantsDropdown
                                parsedRestaurants={restaurants}
                                locationId={location.location_id.toString()}
                                onRestaurantsChange={handleRestaurantsChange}
                                sortCriteria={sortRestaurantsBy}
                                updateSortCriteria={setSortRestaurantsBy}
                            />
                            <HotelsDropdown
                                parsedHotels={hotels}
                                locationId={location.location_id.toString()}
                                onHotelsChange={handleHotelsChange}
                                sortCriteria={sortHotelsBy}
                                updateSortCriteria={setSortHotelsBy}
                            />
                        </div>)} {location && (
                    <div className="mt-1 flex justify-center gap-4 date-picker-group">
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant={"outline"}
                                    style={{
                                        width: "50px",
                                        opacity: "75%"
                                    }}
                                    className={cn(
                                        "pl-3 font-normal",
                                        !startDate && "text-muted-foreground"
                                    )}
                                >
                                    <CalendarIcon className="h-6 w-6" color={COLORS.ORCHID}/>
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent
                                className="w-auto p-0"
                                align="start"
                                side="top"
                            >
                                <Calendar
                                    className="max-w-[400px] overflow-hidden"
                                    mode="single"
                                    selected={startDate || undefined}
                                    onSelect={(date: Date | undefined) => setStartDate(date)}
                                    disabled={(date) => date < new Date()}
                                    initialFocus
                                />
                            </PopoverContent>
                        </Popover>
                        <p className="date-line">-</p>
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant={"outline"}
                                    style={{
                                        width: "50px",
                                        opacity: "75%"
                                    }}
                                    className={cn(
                                        "w-full pl-3 text-left font-normal",
                                        !endDate && "text-muted-foreground"
                                    )}
                                >
                                    <CalendarIcon className="h-6 w-6" color={COLORS.ORCHID}/>
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent
                                className="w-auto p-0"
                                align="start"
                                side="top"
                            >
                                <Calendar
                                    className="max-w-[400px] overflow-hidden"
                                    mode="single"
                                    selected={endDate || undefined}
                                    onSelect={setEndDate}
                                    disabled={(date) => date < new Date()}
                                    initialFocus
                                />
                            </PopoverContent>
                        </Popover>
                    </div>
                )}
                </div>
                {location && (
                    <div className="mt-4 flex justify-center">
                        <Button onClick={onAddTrip} style={styles.addButton}>
                            Add Trip
                        </Button>
                    </div>
                )}
                <div className="suggestions-section">
                    <Suggestions suggestions={suggestions}/>
                </div>
            </div>
            <Toaster/>
        </>
    );
};

export default MainPage;