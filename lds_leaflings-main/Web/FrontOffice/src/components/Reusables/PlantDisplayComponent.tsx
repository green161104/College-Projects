import { useEffect, useRef, useState } from "react";
import { addUserPlant } from "../../services/http/userplant/userPlant-service";
import { getFullImageUrl } from "../../services/http/plant/plant-service";
import AdComponent from "./AdComponent";
import { Plant } from "../../models/plant-model";
import { User } from "../../models/user-model";
import { Ad } from "../../models/Ad";
import { PlantType } from "../../models/enums/PlantType";

interface PlantDisplayComponentProps {
  plant: Plant;
  onClose: () => void;
  userData: User | undefined;
  ad?: Ad;
}

/**
 * PlantDisplayComponent
 *
 * This component displays detailed information about a selected plant, including its image, type, description,
 * and the option to add it to the user's garden. It also shows an ad for users who do not have a paid role.
 *
 * @param {Object} props - The properties passed to this component.
 * @param {Plant} props.plant - The plant to display.
 * @param {Function} props.onClose - Function to close the popup when clicked outside.
 * @param {User | undefined} props.userData - The user data, used to check if the user is paid and to show/hide ads.
 * @param {Ad | undefined} props.ad - The ad to display for unpaid users.
 * @returns {JSX.Element} The plant display component.
 */

const PlantDisplayComponent = ({
  plant,
  onClose,
  userData,
  ad,
}: PlantDisplayComponentProps) => {
  const popupRef = useRef<HTMLDivElement>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  /**
   * Close the popup if clicking outside of it.
   */
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        popupRef.current &&
        !popupRef.current.contains(event.target as Node)
      ) {
        onClose();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [onClose]);

  /**
   * Handles adding the selected plant to the user's garden.
   * This calls the `addUserPlant` service method.
   */
  const handleAddPlant = async () => {
    setErrorMessage(null); // Reset error message before new request

    try {
      await addUserPlant(plant.id);
      alert("Plant successfully added!"); // Notify success
    } catch (error: Error | unknown) {
      // Set the error message to display it
      if (error instanceof Error) {
        setErrorMessage(error.message || "Something went wrong!");
      } else {
        setErrorMessage("Something went wrong!");
      }
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-40 bg-blur-40 backdrop-blur-sm flex items-center justify-center z-50"
      data-testid="plant-display-popup"
    >
      <div
        ref={popupRef}
        className="bg-white w-11/12 md:w-3/4 lg:w-2/3 xl:w-3/4 h-[70vh] rounded-lg p-6 shadow-lg relative flex flex-row gap-6"
      >
        <button onClick={onClose}></button>
        <div className="flex-shrink-0 w-1/6 flex flex-col items-center">
          <figure className="h-56 w-56 rounded-full overflow-hidden justify-center">
            {plant.plantImage && plant.plantImage !== "" ? (
              // Plant image
              <img
                src={getFullImageUrl(plant.plantImage)}
                alt={plant.name}
                className="w-full h-full object-cover"
              />
            ) : (
              // Placeholder \ default Image
              <img
                src="\default_plant_image.jpg"
                alt={plant.name}
                className="w-full h-full object-cover"
              />
            )}
            
          </figure>
          <div className="text-center mt-2">
            <h2 className="text-xl font-bold">{plant.name}</h2>
            <p className="text-gray-700 pt-2">
              Type: {getPlantTypeString(plant.type)}
            </p>
            <button
              className="px-4 py-2 rounded-xl bg-green-600 text-white cursor-pointer mt-4"
              onClick={handleAddPlant}
            >
              Add to My Garden
            </button>

            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
          </div>
        </div>

        <div className="flex-grow flex flex-row gap-6">
          {/* Description box */}
          <div className="h-[25rem] w-2/3 overflow-y-auto border-4 p-4">
            <p className="mb-4 text-xl">{plant.description}</p>
          </div>
          {/* AdComponent */}
          {!userData?.rolePaid && (
            <aside className="w-1/3 flex-shrink-0 p-4 bg-gray-100 rounded-lg shadow-md">
              <AdComponent adItem={ad} />
            </aside>
          )}
        </div>
      </div>
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
export default PlantDisplayComponent;
