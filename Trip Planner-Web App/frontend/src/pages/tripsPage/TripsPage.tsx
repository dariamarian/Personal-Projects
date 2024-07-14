import Navbar from "@/components/Navbar.tsx";
import TripCards from "@/components/TripCards.tsx";

const TripsPage = () => {
    return (
        <div className="flex flex-col">
            <Navbar/>
            <div className="flex justify-center gap-7 mt-10">
                <TripCards />
            </div>
        </div>
    )
}

export default TripsPage