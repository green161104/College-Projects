import { useEffect, useState } from "react";
import { Plant } from "../../models/plant-model";
import PlantMatchesComponent from "./PlantMatchesComponent";
import { matchUserToPlants } from "../../services/http/user/user-service";
import PlantCardComponent from "./PlantCardComponent";
import { User } from "../../models/user-model";
import { Ad } from "../../models/Ad";
import { PlantType } from "../../models/enums/PlantType";

/**
 * Props for the PlantManual component.
 *
 * @interface PlantManualProps
 * @property {User | undefined} userData - Information about the current user.
 * @property {Plant[]} plants - List of all available plants.
 * @property {Ad | undefined} ad - Advertisement or promotional data (optional).
 */
interface PlantManualProps {
  userData?: User; // recebo a userinfo como userData
  plants : Plant[];  // recebo todas as plantas como plants
  ad?: Ad | undefined;
}

/**
 * PlantManual Component
 *
 * This component allows users to explore a plant manual, switch between "All Plants" and "My Matches" views,
 * search for plants, filter them by type, and view personalized plant matches.
 *
 * @param {PlantManualProps} props - Props passed to the component.
 * @returns {JSX.Element} A React functional component for plant manual and match display.
 */
const PlantManual = ({plants, userData, ad}: PlantManualProps) => {
  
  /**
   * State to store categorized matches of plants.
   */
  const [matchedPlants, setMatchedPlants] = useState<{ // calculo matches neste componente pq faz sentido 
    perfectMatches: Plant[] ;
    averageMatches: Plant[] ;
    weakMatches: Plant[] ;
    noMatches: Plant[] ;
    user : User | undefined; 
  }>({
    perfectMatches: [] ,
    averageMatches: [],
    weakMatches: [],
    noMatches: [],
    user : userData,
    }); // Store categorized matches

  /**
   * Loading state to handle asynchronous fetch operations.
   */
  const [loading, setLoading] = useState(true); // faz sentido o loading estar no componente
  
  /**
   * State to toggle between "All Plants" and "My Matches" views.
   */
  const [error, setError] = useState<string | null>(null); // Error state faz sentido estar no componente

   /**
   * Search query state to filter plants by name.
   */
  const [viewType, setViewType] = useState("All"); // toggle between all / my plant matches

  /**
   * Filter type state to filter plants by category/type.
   */
  const [searchQuery, setSearchQuery] = useState(""); // For search - faz sentido estar no component
  
  /**
   * Filter plants based on search query and type.
   * This is applied only when the "All Plants" view is active.
   */
  const [filterType, setFilterType] = useState("All"); // For filtering por tipo - faz sentido pertencer ao componente


  // Fetch matched plants
  useEffect(() => {
    const fetchMatchedPlants = async () => {
      try {
        setLoading(true);
        const data = await matchUserToPlants();
        setMatchedPlants(data);
      } catch (err) {
        console.error("Failed to fetch plant matches:", err);
        setError("Unable to load matched plants.");
      } finally {
        setLoading(false);
      }
    };

    fetchMatchedPlants();
  }, []);

  
  const getPlantTypeString = (
    type: PlantType | string | number | undefined
  ): string => {
    // If it's already a string, try to convert it
    if (typeof type === "string") {
      // Check if it's a valid PlantType string
      const normalizedType =
        type.charAt(0).toUpperCase() + type.slice(1).toLowerCase();
      if (
        Object.values(PlantType).includes(
          normalizedType as unknown as PlantType
        )
      ) {
        return normalizedType;
      }

      // Try to match the string to enum keys
      const enumKey = Object.keys(PlantType).find(
        (key) => key.toLowerCase() === type.toLowerCase()
      );
      return enumKey || type;
    }

    // If it's a number (enum value), convert to string
    if (typeof type === "number") {
      return PlantType[type];
    }

    // Fallback
    return "";
  };

  // Filter plants based on search and type
  const filteredPlants =
    plants?.filter((plant) => {
      // Check if userplant and plant exist
      if (!plant || !plant) return false;

      const matchesSearch = plant.name
        .toLowerCase()
        .includes(searchQuery.toLowerCase());

      const matchesFilter =
        filterType === "All" ||
        getPlantTypeString(plant.type) === filterType;

      return matchesSearch && matchesFilter;
    }) || [];


  return (
    <div className="h-full antialiased">
      <aside className="h-[100%] w-[75%] lg:w-[90%] p-10 bg-white shadow-lg overflow-y-auto ml-20">
        <h2 className="text-xl font-bold mb-4">Plant Manual</h2>

        {/* Loading and Error Handling */}
        {loading && <p>Loading plants...</p>}
        {error && <p className="text-red-500">{error}</p>}

        {/* Toggle View */}
        {!loading && !error && (
          <>
            <div className="flex gap-4 mb-6">
              <button
                onClick={() => setViewType("All")}
                className={`px-4 py-2 rounded-md ${
                  viewType === "All"
                    ? "bg-green-600 text-white"
                    : "bg-gray-200"
                }`}
              >
                All Plants
              </button>
              <button
                onClick={() => setViewType("My Matches")}
                className={`px-4 py-2 rounded-md ${
                  viewType === "My Matches"
                    ? "bg-green-600 text-white"
                    : "bg-gray-200"
                }`}
              >
                My Matches
              </button>
            </div>

            {/* Filters (only for "All Plants") */}
            {viewType === "All" && (
              <div className="mb-6 flex flex-col sm:flex-row justify-between gap-4">
                {/* Search Bar */}
                <input
                  type="text"
                  placeholder="Search plants..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="p-2 border border-gray-300 rounded-md w-full sm:w-3/12"
                />

                {/* Filter Buttons */}
                <div className="flex gap-2">
                  {[
                    "All",
                    "Decorative",
                    "Medicinal",
                    "Fruit",
                    "Vegetable",
                    "Flower",
                    "Succulent",
                  ].map((type) => (
                    <button
                      key={type}
                      onClick={() => setFilterType(type)}
                      className={`px-4 py-2 rounded-md ${
                        filterType === type
                          ? "bg-green-600 text-white"
                          : "bg-gray-200"
                      }`}
                    >
                      {type}
                    </button>
                  ))}
                </div>
              </div>
            )}

            {/* Display Plants */}
            {viewType === "All" ? (
              <div className="grid grid-cols-2 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {filteredPlants?.map((plant) => (
                  <PlantCardComponent plant = {plant} userData={ userData } ad={ad} />
                ))}
              </div>
            ) : (
              <PlantMatchesComponent matches={matchedPlants} userData={ userData } />
            )}
          </>
        )}
      </aside>
    </div>
  );
};

export default PlantManual;
