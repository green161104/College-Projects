import MyAPI from "../tokenInterceptor";



/**
 * Fetches the logs associated with a specific diary by its ID.
 * 
 * @param {number} DiaryId - The ID of the diary whose logs are being fetched.
 * 
 * @returns {Promise<any>} - The data returned by the API, containing the logs for the specified diary.
 * @throws {Error} - Throws an error if the request fails, and logs the error to the console.
 */
export async function getLogsByDiaryId(DiaryId: number) {
  try {
    const response = await MyAPI.get(`/Log/diary/${DiaryId}`);
    return response.data;
  } catch (error) {
    console.log("something went wrong" + error);
  }
}



/**
 * Creates a new log entry for a specific diary.
 * 
 * @param {number} DiaryId - The ID of the diary where the log is being created.
 * @param {string} LogDescription - The description of the log entry being created.
 * 
 * @returns {Promise<any>} - The data returned by the API after creating the log entry.
 * @throws {Error} - Throws an error if the request fails, and logs the error to the console.
 */
export async function createNewLog(DiaryId: number, LogDescription: string) {
  const LogRequestDTO = {
    DiaryId: DiaryId,
    LogDescription: LogDescription,
  };

  try {
    const response = await MyAPI.post(`/Log`, LogRequestDTO);
    return response.data;
  } catch (error) {
    console.log("something went wrong" + error);
  }
}


/**
 * Updates an existing log entry.
 * 
 * @param {number} LogId - The ID of the log being updated.
 * @param {number} DiaryId - The ID of the diary to which the log belongs.
 * @param {string} LogDescription - The updated description for the log.
 * 
 * @returns {Promise<any>} - The data returned by the API after updating the log entry.
 * @throws {Error} - Throws an error if the request fails, and logs the error to the console.
 */
export async function updateLog(LogId : number,DiaryId: number, LogDescription: string) {

    const LogRequestDTO = {
        DiaryId: DiaryId,
        LogDescription: LogDescription,
      };

    try {
      const response = await MyAPI.post(`/Log/diary/${LogId}`, LogRequestDTO);

      return response.data;
    } catch (error) {
      console.log("something went wrong" + error);
    }
  }

  /**
 * Deletes a specific log entry by its ID.
 * 
 * @param {number} LogId - The ID of the log entry being deleted.
 * 
 * @returns {Promise<any>} - The data returned by the API after deleting the log entry.
 * @throws {Error} - Throws an error if the request fails, and logs the error to the console.
 */
  export async function deleteLogById(LogId : number) {
  
  
    try {
      const response = await MyAPI.delete(`/Log/${LogId}`);

      return response.data;
    } catch (error) {
      console.log("something went wrong" + error);
    }
  }