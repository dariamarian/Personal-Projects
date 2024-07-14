import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import './Suggestions.css';
import React from "react";
import { Suggestion } from '@/utils/types';

interface SuggestionsProps {
    suggestions: Suggestion[];
}

const Suggestions: React.FC<SuggestionsProps> = ({suggestions}) => {
    const settings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
    };

    return (
        <div className="suggestions-carousel">
            {suggestions && suggestions.length > 0 && (
                <Slider {...settings}>
                    {suggestions.map((suggestion, index) => (
                        <div key={index} className="suggestion-card">
                            <div className="suggestion-info">
                                <h3>{suggestion.city}</h3>
                                <p>{new Date(suggestion.startDate).toLocaleDateString()} - {new Date(suggestion.endDate).toLocaleDateString()}</p>
                            </div>
                        </div>
                    ))}
                </Slider>
            )}
        </div>
    );
};

export default Suggestions;
