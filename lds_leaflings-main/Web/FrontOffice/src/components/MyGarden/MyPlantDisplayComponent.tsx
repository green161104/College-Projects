/** and displays an advertisement for non-paid users.
 *
 * Props:
 * - userplant: The specific plant associated with the user.
 * - plant: The general plant details (name, type, description, etc.).
 * - onClose: Function to close the component.
 * - userData: The user data, including their subscription role.
 * - adItem: Advertisement data shown to the user if applicable.
 *
 * State:
 * - diaryExists: Indicates if a diary already exists for the user’s plant.
 * - showCreateDiaryPopup: Controls the visibility of the create diary popup.
 * - diaryTitle: Holds the value of the title entered by the user for the new diary.
 *
 * Effects:
 * - Checks if a diary exists when the userplant or userData changes.
 *
 * Event Handlers:
 * - handleClickDiary: Opens the existing diary if available or shows the create diary popup.
 * - handleClosePopup: Closes the create diary popup.
 * - handleCreateDiary: Handles the creation of a new diary.
 * - handleClickOutside: Closes the component if the user clicks outside the popup.
 */
import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import {
  getDiaryByUserPlantId,
  createUserPlantDiary,
} from "../../services/http/diary/diary-service";
import { deleteUserPlant } from "../../services/http/userplant/userPlant-service";
import { getFullImageUrl } from "../../services/http/plant/plant-service";
import { Plant } from "../../models/plant-model";
import { User } from "../../models/user-model";
import { UserPlant } from "../../models/userPlant-model";
import { Ad } from "../../models/Ad";

interface MyPlantDisplayComponentProps {
  userplant: UserPlant;
  plant: Plant;
  onClose: () => void;
  userData: User | undefined;
  adItem?: Ad;
}

const MyPlantDisplayComponent: React.FC<MyPlantDisplayComponentProps> = ({
  userplant,
  plant,
  onClose,
  userData,
}) => {
  const [diaryExists, setDiaryExists] = useState<boolean>(false);
  const [showCreateDiaryPopup, setShowCreateDiaryPopup] = useState(false);
  const [diaryTitle, setDiaryTitle] = useState<string>("");
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);

  const navigate = useNavigate();
  const popupRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (userplant.id && userData) {
      const fetchDiary = async () => {
        try {
          const diary = await getDiaryByUserPlantId(userplant.id!);
          setDiaryExists(!!diary && !!diary.id);
        } catch (error) {
          console.error("Failed to fetch diary:", error);
          setDiaryExists(false);
        }
      };
      fetchDiary();
    }
  }, [userplant.id, userData]);

  const handleClickDiary = () => {
    if (diaryExists) {
      navigate(`/user/mygarden/diary/${plant.id}/${userplant.id}`);
    } else {
      setShowCreateDiaryPopup(true);
    }
  };

  const handleClosePopup = () => {
    setShowCreateDiaryPopup(false);
  };

  const handleCreateDiary = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      await createUserPlantDiary(diaryTitle, userplant.id!);
      setDiaryExists(true);
      setShowCreateDiaryPopup(false);
    } catch (error) {
      console.error("Failed to create diary:", error);
      // Optionally, add user-facing error handling here
    }
  };

  const handleRemovePlant = async () => {
    try {
      await deleteUserPlant(userplant.plant.id);
      onClose(); // Close the component after successful deletion
    } catch (error) {
      console.error("Failed to delete plant:", error);
    }
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        popupRef.current &&
        !popupRef.current.contains(event.target as Node) &&
        !(event.target as Element).closest("form")
      ) {
        onClose();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [onClose]);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 bg-blur-40 backdrop-blur-sm flex items-center justify-center z-50">
      <div
        ref={popupRef}
        className="bg-white w-11/12 md:w-3/4 lg:w-2/3 xl:w-3/4 h-[70vh] rounded-lg p-6 shadow-lg relative flex flex-col gap-4"
      >
        {/* Top section: Image and basic info */}
        <div className="flex gap-6">
          {/* Plant image and info */}
          <div className="flex flex-col items-center">
            <figure className="h-56 w-56 rounded-full overflow-hidden">
              {plant.plantImage && plant.plantImage !== "" ? (
                // Plant Image
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
            <div className="text-center mt-4">
              <h2 className="text-xl font-bold">{plant.name}</h2>
              <p className="text-gray-700 mt-2">Type: {plant.type}</p>
              <button
                className="px-4 py-2 rounded-xl bg-green-600 text-white cursor-pointer mt-4"
                onClick={handleClickDiary}
              >
                {diaryExists ? "View Diary" : "Create Diary"}
              </button>
              <br />
              <button
                className="px-4 py-2 rounded-xl bg-red-600 text-white cursor-pointer mt-4"
                onClick={() => setShowDeleteConfirmation(true)}
              >
                Remove Plant
              </button>
            </div>
          </div>

          {/* Description box */}
          <div className="h-[25rem] w-2/3 overflow-y-auto border-4 p-4">
            <p className="mb-4 text-xl">{plant.description}</p>
          </div>
        </div>

        {/* Create Diary Popup */}
        {showCreateDiaryPopup && (
          <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white w-3/4 max-w-md rounded-lg p-6 shadow-lg">
              <h2 className="text-lg font-bold mb-4">Create New Diary</h2>
              <form onSubmit={handleCreateDiary}>
                <div className="mb-4">
                  <label
                    htmlFor="diaryName"
                    className="block text-sm font-medium text-gray-700"
                  >
                    Diary Name
                  </label>
                  <input
                    type="text"
                    id="diaryName"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-green-500 focus:ring-green-500"
                    placeholder="Enter diary name"
                    value={diaryTitle}
                    onChange={(e) => setDiaryTitle(e.target.value)}
                    required
                  />
                </div>
                <div className="flex justify-end space-x-2">
                  <button
                    type="button"
                    className="px-4 py-2 bg-gray-300 rounded-lg"
                    onClick={handleClosePopup}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-green-600 text-white rounded-lg"
                  >
                    Create
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Delete Confirmation Popup */}
        {showDeleteConfirmation && (
          <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white w-3/4 max-w-md rounded-lg p-6 shadow-lg">
              <h2 className="text-lg font-bold mb-4">Confirm Deletion</h2>
              <p className="mb-4">
                Are you sure you want to remove this plant?
              </p>
              <div className="flex justify-end space-x-2">
                <button
                  className="px-4 py-2 bg-gray-300 rounded-lg"
                  onClick={() => setShowDeleteConfirmation(false)}
                >
                  Cancel
                </button>
                <button
                  className="px-4 py-2 bg-red-600 text-white rounded-lg"
                  onClick={handleRemovePlant}
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MyPlantDisplayComponent;
