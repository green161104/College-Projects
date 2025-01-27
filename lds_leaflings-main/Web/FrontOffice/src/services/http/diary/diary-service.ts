import MyAPI from "../tokenInterceptor";


/**
 * Fetches the diary entries for a specific user plant.
 * 
 * @param {number} UserPlantId - The ID of the user plant whose diary entries are being fetched.
 * 
 * @returns {Promise<any>} - The data returned by the API, containing the diary entries for the user plant.
 * @throws {Error} - Throws an error if the request fails.
 */
export async function getDiaryByUserPlantId(UserPlantId: number) {
  const response = await MyAPI.get(`/Diary/userPlant/${UserPlantId}`);
  return response.data;
}


/**
 * Creates a new diary entry for a specific user plant.
 * 
 * @param {string} Title - The title of the new diary entry.
 * @param {number} UserPlantId - The ID of the user plant for which the diary entry is being created.
 * 
 * @returns {Promise<any>} - The data returned by the API after creating the diary entry.
 * @throws {Error} - Throws an error if the request fails, and logs the error to the console.
 */
export async function createUserPlantDiary(Title: string, UserPlantId: number) {
  const DiaryRequestDTO = {
    title: Title,
    userPlantId: UserPlantId,
  };

  try {
    const response = await MyAPI.post(`/Diary`, DiaryRequestDTO);

    return response.data;
  } catch (error) {
    console.error("Error creating diary:", error);
    return null;
  }
}

/**
 * Deletes a diary entry for a specific user plant.
 * 
 * @param {number} UserPlantId - The ID of the user plant whose diary entry is being deleted.
 * 
 * @returns {Promise<any>} - The data returned by the API after deleting the diary entry.
 * @throws {Error} - Throws an error if the request fails.
 */
export async function deleteUserPlantDiary( UserPlantId : number){

    const response = await MyAPI.delete(`/Diary/${UserPlantId}`)
    return response.data;
}
