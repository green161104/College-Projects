import { MyLocalStorage } from "../../myLocalStorage";
import MyAPI from "../tokenInterceptor";




/**
 * Fetches all plants associated with the currently logged-in user.
 *
 * @returns {Promise<UserPlant[]>} - An array of user plants if available; otherwise, an empty array.
 * @throws {Error} - If the API request fails.
 */
export async function getUserPlants() {
    const userId = MyLocalStorage.getItem("userId");

    const response = await MyAPI.get(`/UserPlants/${userId}`);

    if(response.status == 204) {
        return [];
    }
    
    return response.data;
}


/**
 * Adds a new plant to the logged-in user's collection.
 *
 * @param {number} PlantId - The ID of the plant to be added.
 * @returns {Promise<any>} - The response data from the API upon successfully adding the plant.
 * @throws {Error} - If the user ID is not found in local storage or the API request fails.
 */
export async function addUserPlant(PlantId: number) { 
  const userId = MyLocalStorage.getItem("userId");
  
  const userplantRequest = {
    plantId: PlantId,
    userId: Number(userId), 
  };

  if (!userId) {
    throw new Error("User ID not found in local storage.");
  }

  try {
    const response = await MyAPI.post(`/UserPlants`, userplantRequest);
    return response.data;
  } catch (error: any) {
    console.error("Failed to add UserPlant:", error);

    // Extract error message from the server response
    if (error.response && error.response.data && error.response.data.error) {
      throw new Error(error.response.data.error); // Throw server error for frontend handling
    } else {
      throw new Error("An unexpected error occurred."); // Fallback for unexpected issues
    }
  }
}




  export async function deleteUserPlant(plantId: number) {
    const userId = MyLocalStorage.getItem("userId");

    const response = await MyAPI.delete(`/UserPlants/${userId}/${plantId}`);
    return response.data;
}



/**
 * Fetches all plants associated with the logged-in user by their user ID.
 *
 * @returns {Promise<UserPlant[]>} - An array of user plants associated with the logged-in user.
 * @throws {Error} - If the API request fails.
 */
export async function getPlantsByUserID(){
  const userId = MyLocalStorage.getItem("userId");

  const response = await MyAPI.get(`/UserPlants/${userId}`);
  return response.data;
}