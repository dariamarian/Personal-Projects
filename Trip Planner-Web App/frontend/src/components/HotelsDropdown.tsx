import React, {useEffect, useState} from "react";
import {HotelService} from "@/apis/hotel/HotelService.tsx";
import {Hotel} from "@/utils/types.tsx";
import {components, default as ReactSelect, MultiValue, OptionProps, StylesConfig} from "react-select";
import SortDropdown from "@/components/SortDropdown.tsx";

interface OptionPropsWithHotel extends OptionProps<Hotel, true> {
}

const Option: React.FC<OptionPropsWithHotel> = (props) => {
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

interface HotelsDropdownProps {
    parsedHotels: Hotel[];
    locationId: string;
    onHotelsChange: (selectedOptions: Hotel[]) => void; // Callback function prop
    sortCriteria: string;
    updateSortCriteria: (criteria: string) => void;
}

const HotelsDropdown: React.FC<HotelsDropdownProps> = ({
                                                           parsedHotels,
                                                           locationId, onHotelsChange,
                                                           sortCriteria, updateSortCriteria
                                                       }) => {
    const [hotels, setHotels] = useState<Hotel[]>([]);
    const [selectedHotels, setSelectedHotels] = useState<Hotel[]>([]);

    useEffect(() => {
        const fetchHotels = async () => {
            try {
                let data = parsedHotels;
                if (data.length === 0) {
                    await HotelService.parseHotels(locationId);
                    data = await HotelService.getHotels(locationId);
                }
                if (sortCriteria) {
                    data = await HotelService.sortHotels(locationId, sortCriteria);
                }
                const uniqueHotels = new Map();
                data.forEach((hotel: Hotel) => {
                    uniqueHotels.set(hotel.name, hotel);
                });

                setHotels(Array.from(uniqueHotels.values()));
            } catch (error) {
                console.error("Error fetching hotels:", error);
            }
        };

        if (locationId) {
            fetchHotels();
        }
    }, [locationId, sortCriteria]);

    const handleChange = (newValue: MultiValue<Hotel>) => {
        const selectedOptions = newValue as Hotel[];
        setSelectedHotels(selectedOptions);
        onHotelsChange(selectedOptions);
    };

    const customStyles: StylesConfig<Hotel, true> = {
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
        }),
        menuList: (provided) => ({
            ...provided,
            maxHeight: '250px',
            overflowY: 'auto'
        })
    };

    return (
        <div>
            <div style={{display: "flex", justifyContent: "center"}}>
                <ReactSelect
                    options={hotels.map(hotel => ({
                        value: hotel.hotel_id,
                        label: hotel.name,
                        hotel_id: hotel.hotel_id,
                        locationId: locationId,
                        name: hotel.name,
                        rating: hotel.rating,
                        url: hotel.url,
                        price_level: hotel.price_level,
                        price: hotel.price,
                        latitude: hotel.latitude,
                        longitude: hotel.longitude
                    }))}
                    isMulti
                    closeMenuOnSelect={false}
                    hideSelectedOptions={false}
                    components={{
                        Option
                    }}
                    onChange={handleChange}
                    value={selectedHotels}
                    styles={customStyles}
                    menuPlacement="top"
                    placeholder="Select hotels"
                /><SortDropdown
                sortOptions={[
                    {value: 'name', label: 'name'},
                    {value: 'rating', label: 'rating'},
                    {value: 'price', label: 'price'}
                ]}
                onSelectSort={updateSortCriteria}
            /></div>
        </div>
    );
};


export default HotelsDropdown;