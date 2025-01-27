import { MyLocalStorage } from "../../myLocalStorage";
import MyAPI from "../tokenInterceptor";

const API_URL = import.meta.env.VITE_API_URL;


/**
 * Logs in a user by sending their email and password to the API.
 * 
 * If the login is successful, the user's authentication token and user ID are stored
 * in localStorage for future use.
 * 
 * @param {string} email - The user's email address.
 * @param {string} password - The user's password.
 * 
 * @returns {Promise<any>} - The data returned from the API after login attempt.
 * @throws {Error} - Throws an error if the request fails.
 */
export async function login(email: string, password: string) {
  const response = await MyAPI.post(`/Auth/login`, {
    email,
    password,
  });
  const data = response.data;

  if (data.auth) {
    MyLocalStorage.setItem("authToken", data.token);
    MyLocalStorage.setItem("userId", data.userId.toString());
  }

  return data;
}

interface RegisterFormData {
  username: string;
  email: string;
  password: string;
  location: string;
  contact: string;
  careExperience: string;
  waterAvailability: string;
  luminosityAvailability: string;
}


/**
 * Registers a new user by sending their registration information to the API.
 * 
 * The user data is sent as a FormData object containing the user's details such as
 * username, email, password, and other plant care-related information.
 * 
 * @param {RegisterFormData} payload - The registration data for the new user.
 * @param {string} payload.username - The username of the new user.
 * @param {string} payload.email - The email address of the new user.
 * @param {string} payload.password - The password for the new user.
 * @param {string} payload.location - The location of the new user.
 * @param {string} payload.contact - The contact information for the new user.
 * @param {string} payload.careExperience - The user's plant care experience.
 * @param {string} payload.waterAvailability - The user's water availability for plants.
 * @param {string} payload.luminosityAvailability - The user's luminosity (light) availability for plants.
 * 
 * @returns {Promise<void>} - Resolves when the registration is complete.
 * @throws {Error} - Throws an error if the registration request fails.
 */

export async function register(payload: RegisterFormData) {

  const formData = new FormData();
  formData.append("username", payload.username);
  formData.append("email", payload.email);
  formData.append("password", payload.password);
  formData.append("location", payload.location);
  formData.append("contact", payload.contact);
  formData.append("careExperience", payload.careExperience);
  formData.append("waterAvailability", payload.waterAvailability);
  formData.append("luminosityAvailability", payload.luminosityAvailability);


  try {
    const response = await fetch(`${API_URL}/User`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    return await response.json(); // return response.json()
  } catch (error) {
    console.error('Error during request:', error);
    throw error;
  }
}

/**
 * Checks if the user is logged in by verifying the authentication token.
 * 
 * @returns {Promise<boolean>} - Returns a promise that resolves to `true` if the user is authenticated, otherwise `false`.
 * 
 * This function performs the following steps:
 * 1. Retrieves the authentication token from local storage.
 * 2. If the token is not found, it returns `false`.
 * 3. Sends a GET request to the authentication endpoint with the token.
 * 4. If the response indicates that the user is authenticated, it updates the token and user ID in local storage and returns `true`.
 * 5. If the response indicates that the user is not authenticated, it returns `false`.
 */
export async function isLoggedIn() {
  const authToken = MyLocalStorage.getItem("authToken");

  if (!authToken) {
    return false;
  }

  const response = await MyAPI.get(`/Auth/${authToken}`);
  const data = response.data;

  if (data.auth) {
    MyLocalStorage.setItem("authToken", data.token);
    MyLocalStorage.setItem("userId", data.userId.toString());
    return true;
  }

  return false;

}

/**
 * Logs out the user by removing the authentication token and user ID from local storage.
 * 
 * This function performs the following steps:
 * 1. Removes the `authToken` from local storage.
 * 2. Removes the `userId` from local storage.
 */
export function logout() {
  localStorage.removeItem("authToken");
  localStorage.removeItem("userId");
}
