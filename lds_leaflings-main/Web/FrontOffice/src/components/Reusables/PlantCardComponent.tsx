import { useState } from "react";
import { Plant } from "../../models/plant-model";
import PlantDisplayComponent from "./PlantDisplayComponent";
import { User } from "../../models/user-model";
import { getFullImageUrl } from "../../services/http/plant/plant-service";
import { Ad } from "../../models/Ad";
import { PlantType } from "../../models/enums/PlantType";

interface PlantCardComponentProps {
  userData?: User; // IserInfo
  plant: Plant; // all Plants
  ad?: Ad | undefined;
}

const PlantCardComponent = ({
  plant,
  userData,
  ad,
}: PlantCardComponentProps) => {
  const [selectedPlant, setSelectedPlant] = useState<Plant | undefined>(
    undefined
  );

  const handleCardClick = () => {
    setSelectedPlant(plant);
  };

  const closePopup = () => {
    setSelectedPlant(undefined);
  };

  return (
    <div
      data-testid="plant-card"
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
            // Plant image
            <img
              className="relative w-40"
              src={getFullImageUrl(plant.plantImage)}
              alt={plant.name}
            />
          ) : (
            // Default plant image
            <img
              src="/default_plant_image.jpg"
              className="relative w-40"
              alt={plant.name}
            />
          )}

        </div>

        <div className="relative text-white px-6 pb-6 mt-6">
          <span className="block opacity-75 -mb-1">
            {getPlantTypeString(plant.type)}
          </span>
          <div className="flex justify-between">
            <span className="block font-semibold text-xl">{plant.name}</span>
          </div>
        </div>

      </div>

      {selectedPlant && (
        <PlantDisplayComponent
          userData={userData}
          plant={selectedPlant}
          onClose={closePopup}
          ad={ad}
        />
      )}
    </div>
  );
};

const getPlantTypeString = (
  type: PlantType | string | number | undefined
): string => {
  // If it's already a string, try to convert it
  if (typeof type === "string") {
    // Check if it's a valid PlantType string
    const normalizedType =
      type.charAt(0).toUpperCase() + type.slice(1).toLowerCase();
    if (
      Object.values(PlantType).includes(normalizedType as unknown as PlantType)
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

export default PlantCardComponent;
