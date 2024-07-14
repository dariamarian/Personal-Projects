import React, {useEffect, useState} from "react";
import {RestaurantService} from "@/apis/restaurant/RestaurantService.tsx";
import {Restaurant} from "@/utils/types.tsx";
import {components, default as ReactSelect, MultiValue, OptionProps, StylesConfig} from "react-select";
import SortDropdown from "@/components/SortDropdown.tsx";

interface OptionPropsWithRestaurant extends OptionProps<Restaurant, true> {
}

const Option: React.FC<OptionPropsWithRestaurant> = (props) => {
    return (
        <div>
            <components.Option {...props}>
                <input
                    type="checkbox"
                    checked={props.isSelected}
                    onChange={() => null}
                />{" "}
                <label>{props.label}</label>
            </components.Option>
        </div>
    );
};

interface RestaurantsDropdownProps {
    parsedRestaurants: Restaurant[];
    locationId: string;
    onRestaurantsChange: (selectedOptions: Restaurant[]) => void; // Callback function prop
    sortCriteria: string;
    updateSortCriteria: (criteria: string) => void;
}

const RestaurantsDropdown: React.FC<RestaurantsDropdownProps> = ({
                                                                     parsedRestaurants,
                                                                     locationId, onRestaurantsChange,
                                                                     sortCriteria, updateSortCriteria
                                                                 }) => {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [selectedRestaurants, setSelectedRestaurants] = useState<Restaurant[]>([]);

    useEffect(() => {
        const fetchRestaurants = async () => {
            try {
                let data = parsedRestaurants;
                if (data.length === 0) {
                    await RestaurantService.parseRestaurants(locationId);
                    data = await RestaurantService.getRestaurants(locationId);
                }
                if (sortCriteria) {
                    data = await RestaurantService.sortRestaurants(locationId, sortCriteria);
                }
                const uniqueRestaurants = new Map();
                data.forEach((restaurant: Restaurant) => {
                    uniqueRestaurants.set(restaurant.name, restaurant);
                });

                setRestaurants(Array.from(uniqueRestaurants.values()));
            } catch (error) {
                console.error("Error fetching restaurants:", error);
            }
        };

        if (locationId) {
            fetchRestaurants();
        }
    }, [locationId, sortCriteria]);

    const handleChange = (newValue: MultiValue<Restaurant>) => {
        const selectedOptions = newValue as Restaurant[];
        setSelectedRestaurants(selectedOptions);
        onRestaurantsChange(selectedOptions);
    };

    const customStyles: StylesConfig<Restaurant, true> = {
        control: (provided) => ({
            ...provided,
            backgroundColor: 'lightgrey',
            backgroundOpacity: '0.9',
            borderColor: '#3cacae',
            borderWidth: '2px',
            padding: '5px',
            width: '400px',
            margin: 'auto',
            opacity: '0.8',
        }), menuList: (provided) => ({
            ...provided,
            maxHeight: '250px',
            overflowY: 'auto'
        })
    };

    return (
        <div>
            <div style={{display: "flex", justifyContent: "center"}}>
                <ReactSelect
                    options={restaurants.map(restaurant => {
                        return {
                            value: restaurant.restaurant_id,
                            label: restaurant.name,
                            restaurant_id: restaurant.restaurant_id,
                            locationId: locationId,
                            name: restaurant.name,
                            rating: restaurant.rating,
                            phone: restaurant.phone,
                            email: restaurant.email,
                            url: restaurant.url,
                            price_level: restaurant.price_level,
                            cuisine: restaurant.cuisine,
                            latitude: restaurant.latitude,
                            longitude: restaurant.longitude
                        };
                    })}
                    isMulti
                    closeMenuOnSelect={false}
                    hideSelectedOptions={false}
                    components={{
                        Option
                    }}
                    onChange={handleChange}
                    value={selectedRestaurants}
                    styles={customStyles}
                    menuPlacement="top"
                    placeholder="Select restaurants"
                /><SortDropdown
                sortOptions={[
                    {value: 'name', label: 'name'},
                    {value: 'rating', label: 'rating'}
                ]}
                onSelectSort={updateSortCriteria}
            /></div>
        </div>
    );
};


export default RestaurantsDropdown;