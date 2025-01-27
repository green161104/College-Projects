import { User, UserPreferences } from "../../../models/user-model";
import { MyLocalStorage } from "../../myLocalStorage";
import { getFullImageUrl } from "../plant/plant-service";
import MyAPI from "../tokenInterceptor";


/**
 * Fetches data of all users.
 * 
 * This function makes a request to retrieve data of all users. It maps the data to include 
 * the full URL for each user's avatar.
 * 
 * @returns {Promise<any[]>} - A promise that resolves to an array of users, 
 * with each user including a full URL for their avatar.
 * @throws {Error} - Throws an error if the request fails.
 */
export async function getUserData() {
  try {
    const userId = MyLocalStorage.getItem("userId");
    const response = await MyAPI.get(`/User/${userId}`);

    if (!response.data || response.data.length === 0) {
      console.error("Expected a 'data' array but got:", response.data);
      return [];
    }
    console.log("return from api:", response.data)
    return response.data
  } catch (error) {
    console.error("Error fetching all user data:", error);
    throw error;
  }
}


/**
 * Updates the user preferences for the logged-in user.
 * 
 * This function makes a request to update the user preferences of the currently authenticated user.
 * 
 * @param {UserPreferences} newUserPreferences - The new preferences to be saved for the user.
 * @returns {Promise<any>} - A promise that resolves to the updated user preferences.
 */
export async function updateUserPreferences(
  newUserPreferences: UserPreferences
) {
  const userId = MyLocalStorage.getItem("userId");
  const response = await MyAPI.put(
    `/User/preferences/${userId}`,
    newUserPreferences
  );
  return response.data;
}



/**
 * Updates the information of the logged-in user.
 * 
 * This function makes a request to update the user data for the currently authenticated user.
 * 
 * @param {User} newUserData - The new data for the user, including username, location, and contact.
 * @returns {Promise<any>} - A promise that resolves to the updated user data, including the full URL for the avatar.
 */
export async function updateUserData(newUserData: User) {

  const userId = MyLocalStorage.getItem("userId"); // get the user who's logged in ID
  const response = await MyAPI.put(`/User/${userId}`, newUserData);

  return {
    ...response.data,
    userAvatar: getFullImageUrl(response.data.userAvatar), // Ensure full URL
  };
}


/**
 * Matches the logged-in user to their corresponding plants.
 * 
 * This function makes a request to find plants that match the logged-in user's preferences and 
 * information.
 * 
 * @returns {Promise<any[]>} - A promise that resolves to an array of plants that match the user.
 */
export async function matchUserToPlants() {
  const userId = MyLocalStorage.getItem("userId"); // get the user who's logged in ID
  const response = await MyAPI.get(`/User/match/${userId}`); // call the end point

  if (!response.data || response.data.length === 0) {
    // is the response null or is there no data?
    console.log("there are no matches for this user");
    return [];
  }
  return response.data; // otherwise return the data
}


/**
 * Updates the profile picture of the logged-in user.
 * 
 * This function makes a request to update the profile picture of the currently authenticated user.
 * 
 * @param {File} imageFile - The image file to be uploaded as the new profile picture.
 * @returns {Promise<any>} - A promise that resolves to the response data.
 * @throws {Error} - Throws an error if the user ID is not found in local storage.
 */
export async function updateProfilePicture(imageFile: File): Promise<any> {
  const userId = MyLocalStorage.getItem("userId");
  if (!userId) {
    throw new Error("User ID not found in local storage");
  }

  const formData = new FormData();
  formData.append("image", imageFile);

  const response = await MyAPI.put(`/User/image/${userId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response.data; // Return the response data for further use
}



/**
 * Updates the personal information (username, location, contact) of the logged-in user.
 * 
 * This function makes a request to update the user information for the currently authenticated user.
 * 
 * @param {string} username - The new username to be set.
 * @param {string} location - The new location to be set.
 * @param {string} contact - The new contact information to be set.
 * @returns {Promise<any>} - A promise that resolves to the updated user data.
 */
export async function updateUserInformation(username: string, location: string, contact: string): Promise<any> {
  const userId = MyLocalStorage.getItem("userId");
  const response = await MyAPI.put(`/User/${userId}`, {
    username,
    location,
    contact
  });

  return response.data; // Return response to handle success messages or errors
}


/**
 * Updates the password for the logged-in user.
 * 
 * This function makes a request to update the user's password for the currently authenticated user.
 * 
 * @param {string} oldPassword - The current password of the user.
 * @param {string} newPassword - The new password to set for the user.
 * @returns {Promise<any>} - A promise that resolves to the updated user data.
 */
export async function updatePassword(oldPassword: string, newPassword: string) {
  const userId = MyLocalStorage.getItem("userId");
  const response = await MyAPI.put(`/User/password/${userId}`, {
    oldPassword,
    newPassword
  })

  return response.data
}