import DonationCards from "@/components/DonationCards"
import Navbar from "@/components/Navbar"


const DonationsPage = () => {
  return (
    <div className="flex flex-col">
      <Navbar />
      <div className="flex justify-center gap-7 mt-10">
        <DonationCards />
      </div>
    </div>
  )
}

export default DonationsPage