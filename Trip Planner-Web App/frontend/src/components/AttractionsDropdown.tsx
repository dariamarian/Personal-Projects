import React, {useEffect, useState} from "react";
import {AttractionService} from "@/apis/attraction/AttractionService";
import {Attraction} from "@/utils/types";
import {components, default as ReactSelect, MultiValue, OptionProps, StylesConfig} from "react-select";
import SortDropdown from "@/components/SortDropdown.tsx";

interface OptionPropsWithAttraction extends OptionProps<Attraction, true> {
}

const Option: React.FC<OptionPropsWithAttraction> = (props) => {
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

interface AttractionsDropdownProps {
    parsedAttractions: Attraction[];
    locationId: string;
    onAttractionsChange: (selectedOptions: Attraction[]) => void;
    sortCriteria: string;
    updateSortCriteria: (criteria: string) => void; // Function to update the sorting criteria
}

const AttractionsDropdown: React.FC<AttractionsDropdownProps> = ({
                                                                     parsedAttractions,
                                                                     locationId, onAttractionsChange,
                                                                     sortCriteria, updateSortCriteria
                                                                 }) => {
    const [attractions, setAttractions] = useState<Attraction[]>([]);
    const [selectedAttractions, setSelectedAttractions] = useState<Attraction[]>([]);

    useEffect(() => {
        const fetchAttractions = async () => {
            try {
                let data = parsedAttractions;
                if (data.length === 0) {
                    await AttractionService.parseAttractions(locationId);
                    data = await AttractionService.getAttractions(locationId);
                }
                if (sortCriteria) {
                    data = await AttractionService.sortAttractions(locationId, sortCriteria);
                }
                const uniqueAttractions = new Map();
                data.forEach((attraction: Attraction) => {
                    uniqueAttractions.set(attraction.name, attraction);
                });

                setAttractions(Array.from(uniqueAttractions.values()));
            } catch (error) {
                console.error("Error fetching attractions:", error);
            }
        };

        if (locationId) {
            fetchAttractions();
        }
    }, [locationId, sortCriteria]);

    const handleChange = (newValue: MultiValue<Attraction>) => {
        const selectedOptions = newValue as Attraction[];
        setSelectedAttractions(selectedOptions);
        onAttractionsChange(selectedOptions);
    };

    const customStyles: StylesConfig<Attraction, true> = {
        control: (provided) => ({
            ...provided,
            backgroundColor: 'lightgrey',
            opacity: '0.9',
            borderColor: '#3cacae',
            borderWidth: '2px',
            padding: '5px',
            width: '400px',
            margin: 'auto'
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
                    options={attractions.map(attraction => ({
                        value: attraction.attraction_id,
                        label: attraction.name,
                        attraction_id: attraction.attraction_id,
                        locationId: locationId,
                        name: attraction.name,
                        url: attraction.url,
                        rating: attraction.rating,
                        latitude: attraction.latitude,
                        longitude: attraction.longitude
                    }))}
                    isMulti
                    closeMenuOnSelect={false}
                    hideSelectedOptions={false}
                    components={{
                        Option
                    }}
                    onChange={handleChange}
                    value={selectedAttractions}
                    styles={customStyles}
                    menuPlacement="top"
                    placeholder="Select attractions"
                />
                <SortDropdown
                    sortOptions={[
                        {value: 'name', label: 'name'},
                        {value: 'rating', label: 'rating'}
                    ]}
                    onSelectSort={updateSortCriteria}
                /></div>
        </div>
    );
};


export default AttractionsDropdown;