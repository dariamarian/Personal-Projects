import FavoriteCards from "@/components/FavoriteCards"
import Navbar from "@/components/Navbar.tsx"

const FavoritesPage = () => {
  return (
    <div className="flex flex-col">
      <Navbar />
      <div className="flex justify-center gap-7 mt-10">
        <FavoriteCards />
      </div>
    </div>
  )
}

export default FavoritesPage