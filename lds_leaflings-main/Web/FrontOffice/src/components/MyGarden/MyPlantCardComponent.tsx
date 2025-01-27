/**
 * MyPlantCardComponent
 * This component displays a card representing a plant with its image, type, and name.
 * When clicked, it opens a popup to show more details of the selected plant, including
 * options to view or create a diary. The component also conditionally renders an ad
 * for non-paid users.
 *
 * Props:
 * - plant: The specific plant details (name, type, image, etc.).
 * - user: The user data associated with the plant.
 * - userplant: The relationship between the user and the plant (includes user-specific data).
 * - adItem: Advertisement data shown to the user if applicable.
 *
 * State:
 * - selectedPlant: Holds the currently selected plant for displaying in the popup.
 * - userData: The user data passed as a prop, used to determine the user's role.
 *
 * Event Handlers:
 * - handleCardClick: Opens the popup when the card is clicked.
 * - closePopup: Closes the popup and resets the selected plant state.
 */
import { useState } from "react";
import { Plant } from "../../models/plant-model";
import { User } from "../../models/user-model";
import MyPlantDisplayComponent from "./MyPlantDisplayComponent";
import { UserPlant } from "../../models/userPlant-model";
import { getFullImageUrl } from "../../services/http/plant/plant-service";

interface MyPlantCardComponentProps {
  plant: Plant;
  user: User | undefined;
  userplant: UserPlant;
  onUpdate: () => Promise<void>;
}

const MyPlantCardComponent: React.FC<MyPlantCardComponentProps> = ({
  plant,
  user,
  userplant,
  onUpdate,
}) => {
  const [selectedPlant, setSelectedPlant] = useState<UserPlant | null>(null);
  const [userData] = useState<User | undefined>(user); // recebo user aqui

  /**
   * Event handler to open the popup when the card is clicked.
   */
  const handleCardClick = () => {
    setSelectedPlant(userplant);
  };

  /**
   * Closes the popup by resetting the selected plant state.
   */
  const closePopup = () => {
    setSelectedPlant(null);
  };

  return (
    <div
      className="flex-shrink-0 relative overflow-hidden bg-green-400 rounded-lg shadow-lg max-w-xs cursor-pointer"
      onClick={handleCardClick} // Open popup on click
    >
      <div>
        <svg
          className="absolute bottom-0 left-0 mb-8"
          viewBox="0 0 375 283"
          fill="none"
          style={{ transform: "scale(1.5)", opacity: 0.1 }}
        >
          <rect
            x="159.52"
            y="175"
            width="152"
            height="152"
            rx="8"
            transform="rotate(-45 159.52 175)"
            fill="white"
          />
          <rect
            y="107.48"
            width="152"
            height="152"
            rx="8"
            transform="rotate(-45 0 107.48)"
            fill="white"
          />
        </svg>
        <div className="relative pt-10 px-10 flex items-center justify-center">
          <div
            className="block absolute w-48 h-48 bottom-0 left-0 -mb-24 ml-3"
            style={{
              background: "radial-gradient(black, transparent 60%)",
              transform: "rotate3d(0, 0, 1, 20deg) scale3d(1, 0.6, 1)",
              opacity: 0.2,
            }}
          ></div>

          {plant.plantImage && plant.plantImage !== "" ? (
            // Plant Image
            <img
              className="relative w-40"
              src={getFullImageUrl(plant.plantImage)}
              alt={plant.name}
            />
          ) : (
            // Placeholder \ default Image
            <img
              src="\default_plant_image.jpg"
              className="relative w-40"
              alt={plant.name}
            />
          )}
        </div>
        <div className="relative text-white px-6 pb-6 mt-6">
          <span className="block opacity-75 -mb-1">{plant.type}</span>
          <div className="flex justify-between">
            <span className="block font-semibold text-xl">{plant.name}</span>
          </div>
        </div>
      </div>

      {selectedPlant && (
        <MyPlantDisplayComponent
          userplant={selectedPlant}
          userData={userData}
          plant={selectedPlant.plant}
          onClose={() => {
            closePopup();
            onUpdate();
          }}
        />
      )}
    </div>
  );
};

export default MyPlantCardComponent;
