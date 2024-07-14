import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger,} from "@/components/ui/dropdown-menu";
import {ChevronDown, Heart} from "lucide-react";
import {FaPlane} from "react-icons/fa";
import {useEffect, useState} from "react";
import {capitalizeString} from "@/lib/utils.ts";
import {Link} from "react-router-dom";
import SandwichMenu from "./HamburgerMenu";
import {AuthService} from "@/apis/auth/AuthService.tsx";

const NavbarMainPage = () => {
    const [username, setUsername] = useState("Username");


    useEffect(() => {
        const username = localStorage.getItem("username");
        setUsername(capitalizeString(username || "") || "Username");
    }, []);

    return (
        <>
            <div className="z-1 border-gray-200 navbar-bg-important h-[80px] w-full justify-center overflow-hidden border-b-2">
                <div className="flex h-[80px] flex-row items-center justify-between px-10 ">
                    <div className="flex items-center gap-2">
                        <Link to="/" className="flex items-center justify-center">
                            <img
                                src="/logo2.png"
                                alt="img"
                                className="h-20 w-30 overflow-hidden"
                            />
                            <h1 className="hidden text-xl font-bold lg:block lg:text-lg">
                                Trip Planner
                            </h1>
                        </Link>
                    </div>

                    <div className="hidden items-center gap-4 md:flex">
                        <a href="/favorites" className="">
                            <div className="flex items-center">
                                <Heart className="mr-1 h-4 w-4"/>
                                <span className="lg:text-lg text-base">Favourites</span>
                            </div>
                        </a>
                        <a href="/trips">
                            <div className="flex items-center">
                                <FaPlane className="mr-1 h-4 w-4"/>
                                <span className="lg:text-lg text-base">Trips</span>
                            </div>
                        </a>

                        <DropdownMenu>
                            <DropdownMenuTrigger>
                                <div className="flex items-center gap-[2px]">
                                    <span className="lg:text-lg text-base">{username}</span>
                                    <ChevronDown className="h-4 w-4"/>
                                </div>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent>
                                <Link to="/profile">
                                    <DropdownMenuItem>Profile</DropdownMenuItem>
                                </Link>
                                <DropdownMenuItem onClick={
                                    username == "Username" ? AuthService.loginPage :
                                        AuthService.logout
                                }>{username == "Username" ? "Sign-in" : "Logout"}</DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </div>
                    <div className="flex items-center justify-center md:hidden">
                        <SandwichMenu/>
                    </div>
                </div>
            </div>
        </>
    );
};

export default NavbarMainPage;
