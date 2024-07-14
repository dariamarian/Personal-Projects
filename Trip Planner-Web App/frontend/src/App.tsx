import MainPage from "./pages/mainPage/MainPage";
import { Suspense } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import SignInPage from "./pages/auth/SignInPage";
import SignUpPage from "./pages/auth/SignUpPage";
import { Toaster } from "sonner";
import UserProfilePage from "./pages/userPage/UserProfilePage";
import WeatherPage from "@/pages/weatherPage/WeatherPage";
import MapPage from "@/pages/mapPage/MapPage.tsx";
import TripsPage from "@/pages/tripsPage/TripsPage.tsx";
import FavoritesPage from "./pages/tripsPage/FavoritesPage";

function App() {
    return (
        <>
                <BrowserRouter>
                    <Suspense fallback={<div>Loading...</div>}>
                        <Toaster />
                        <Routes>
                            <Route path="/" element={<MainPage />} />
                            <Route path="/sign-in" element={<SignInPage />} />
                            <Route path="/sign-up" element={<SignUpPage />} />
                            <Route path="/profile" element={<UserProfilePage />} />
                            <Route path="/weather/:locationName" element={<WeatherPage />} />
                            <Route path="/map/:locationId" element={<MapPage />} />
                            <Route path="/trips" element={<TripsPage/>} />
                            <Route path="/favorites" element={<FavoritesPage />} />
                        </Routes>
                    </Suspense>
                </BrowserRouter>
        </>
    );
}

export default App;
