import React, { useState, useRef, useEffect, FC, CSSProperties } from "react";
import { SortAscIcon } from "lucide-react";

interface SortOption {
    value: string;
    label: string;
}

interface SortDropdownProps {
    sortOptions: SortOption[];
    onSelectSort: (value: string) => void;
}

const useOutsideClick = (ref: React.RefObject<HTMLDivElement>, callback: () => void) => {
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (ref.current && !ref.current.contains(event.target as Node)) {
                callback();
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [ref, callback]);
};

const SortDropdown: FC<SortDropdownProps> = ({ sortOptions, onSelectSort }) => {
    const [isOpen, setIsOpen] = useState(false);
    const wrapperRef = useRef<HTMLDivElement>(null);
    useOutsideClick(wrapperRef, () => setIsOpen(false));

    const toggleDropdown = () => setIsOpen(!isOpen);

    const buttonStyle: CSSProperties = {
        border: 'none',
        cursor: 'pointer',
        padding: '8px',
        borderRadius: '8px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    };

    const dropdownStyle: CSSProperties = {
        position: 'absolute',
        bottom: '100%',
        left: 0,
        background: 'rgba(255, 255, 255, 0.9)',
        borderRadius: '8px',
        boxShadow: '0 2px 10px rgba(0,0,0,0.2)',
        width: '80px',
        listStyle: 'none',
        padding: 0,
        margin: '4px 0 0',
        zIndex: 10,
    };

    const itemStyle: CSSProperties = {
        padding: '10px',
        cursor: 'pointer',
        color: '#333',
    };

    return (
        <div ref={wrapperRef} style={{ position: 'relative', display: 'inline-block' }}>
            <button onClick={toggleDropdown} style={buttonStyle}>
                <SortAscIcon size={40} color={"#C26DBC"} />
            </button>
            {isOpen && (
                <ul style={dropdownStyle}>
                    {sortOptions.map(option => (
                        <li
                            key={option.value}
                            style={itemStyle}
                            onClick={() => {
                                onSelectSort(option.value);
                                setIsOpen(false);
                            }}
                        >
                            {option.label}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default SortDropdown;
