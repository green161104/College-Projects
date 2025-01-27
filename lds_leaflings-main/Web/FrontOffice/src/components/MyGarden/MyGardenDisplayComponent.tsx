/**
 * MyGardenDisplayComponent
 * This component displays a grid of the user's plants. It includes a search bar
 * for filtering plants by name and a filter for selecting plant types (e.g.,
 * decorative, medicinal, fruit, etc.). It uses `MyPlantCardComponent` to render
 * individual plant cards, with the option to show ads for non-paid users.
 *
 * Props:
 * - userplants: An array of user-plant relationships, each containing plant data.
 * - userData: The user data, used for determining user-specific behavior or displaying personalized information.
 * - adItem: Advertisement data that can be shown to the user if applicable.
 *
 * State:
 * - searchQuery: The search input used to filter plants by name.
 * - filterType: The selected plant type for filtering plants (e.g., "All", "Fruit", etc.).
 *
 * Helper Functions:
 * - getPlantTypeString: Converts a plant type to a string (handling both string and number types).
 *
 * Event Handlers:
 * - setSearchQuery: Updates the search query state when the user types in the search input.
 * - setFilterType: Updates the selected filter type when the user clicks a filter button.
 *
 * Rendering Logic:
 * - Filters the `userplants` based on the `searchQuery` and `filterType`.
 * - Renders a grid of plant cards using `MyPlantCardComponent`.
 * - Displays a message if no plants match the search and filter criteria.
 */
import { useEffect, useState } from "react";
import { UserPlant } from "../../models/userPlant-model";
import MyPlantCardComponent from "./MyPlantCardComponent";
import { User } from "../../models/user-model";
import { PlantType } from "../../models/enums/PlantType";

interface GardenDisplayComponentProps {
  userplants: UserPlant[] | undefined;
  userData: User | undefined;
  onUpdate: () => Promise<void>;
}

const MyGardenDisplayComponent = ({userplants, userData, onUpdate}: GardenDisplayComponentProps) => {
  const [searchQuery, setSearchQuery] = useState(""); // For search input
  const [filterType, setFilterType] = useState("All"); // For filtering by type
  const [localUserPlants, setLocalUserPlants] = useState(userplants);

  useEffect(() => {
    setLocalUserPlants(userplants);
  }, [userplants]);

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
  const filteredPlants = () => {
    if (!localUserPlants) {
      return [];
    }

    return localUserPlants?.filter((userplant) => {
      if (!userplant || !userplant.plant) return false;

      const matchesSearch = userplant.plant.name
        .toLowerCase()
        .includes(searchQuery.toLowerCase());

      const matchesFilter =
        filterType === "All" ||
        getPlantTypeString(userplant.plant.type) === filterType;

      return matchesSearch && matchesFilter;
    });
  };


  return (
    <div className="h-full">
      <aside className="h-[100%] w-[75%] lg:w-[90%] p-10 bg-white shadow-lg overflow-y-auto ml-20">
        <h2 className="text-xl font-bold mb-4">Plant Grid</h2>

        {/* Search and Filter Controls */}
        <div className="mb-6 flex flex-col sm:flex-row justify-between gap-4">
          {/* Search Bar */}
          <input
            type="text"
            placeholder="Search plants..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="p-2 border border-gray-300 rounded-md w-full sm:w-1/2"
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

        {/* Plant Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-2 lg:grid-cols-3 gap-8 rounded-md">
          {filteredPlants().length > 0 ? (
            filteredPlants().map((userplant: UserPlant, index) => {
              if (!userplant || !userplant.plant) {
                console.warn(
                  `Missing Plant data for userplant at index ${index}`,
                  userplant
                );
                return null; // Skip rendering for invalid data
              }

              return (
                <MyPlantCardComponent
                  key={index}
                  plant={userplant.plant}
                  user={userData}
                  userplant={userplant}
                  onUpdate={onUpdate}
                />
              );
            })
          ) : (
            // Render an empty grid or a placeholder when there are no userplants
            <div className="col-span-2 sm:col-span-2 lg:col-span-3 text-center">
              No plants to display.
            </div>
          )}
        </div>
      </aside>
    </div>
  );
};

export default MyGardenDisplayComponent;
