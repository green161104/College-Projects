import { PlantType } from "../../../models/enums/PlantType";
import { Plant } from "../../../models/plant-model";
import MyAPI from "../tokenInterceptor";

const SERVER_URL = import.meta.env.VITE_SERVER_URL;

/**
 * Constructs the full URL for a plant image.
 * 
 * If the provided `relativeUrl` is not an absolute URL (does not start with "http"), 
 * it prepends the API base URL to it.
 * 
 * @param {string} relativeUrl - The relative URL of the plant image.
 * @returns {string} - The full URL of the image, either by prepending the base API URL or returning the original URL if it's already absolute.
 */
export function getFullImageUrl(relativeUrl: string) {
    return `${SERVER_URL}/${relativeUrl}`;
}

// getAllPlants endpoints, sem paginação
/**
 * Fetches all plants from the server.
 * 
 * This function makes a request to get all plants without pagination and includes 
 * the full image URL for each plant. If no data or an invalid response is received, 
 * it returns an empty array.
 * 
 * @returns {Promise<any[]>} - A promise that resolves to an array of plants, 
 * with each plant including a full image URL and the type mapped from the `PlantType` enum.
 */
export async function getAllPlants() {
    const response = await MyAPI.get(`/Plant`);

    if (!response.data || !response.data.data) {
        console.error("Expected a 'data' array but got:", response.data);
        return [];
    }

    // Map plants to include the full image URL
    return response.data.data.map((plant: Plant) => ({
        ...plant,
        plantImage: `${plant.plantImage}`,
        type: PlantType[plant.type]
    }));
}

/**
 * Fetches details of a specific plant by its ID.
 * 
 * This function makes a request to retrieve data for a plant with the given `plantId`.
 * 
 * @param {number} plantId - The ID of the plant to fetch.
 * @returns {Promise<any>} - A promise that resolves to the plant data.
 */
export async function getPlant(plantId: number) {
    const response = await MyAPI.get(`/Plant/${plantId}`);
    return response.data;
}


/**
 * Searches for plants by their name.
 * 
 * This function makes a request to search for plants with a name that matches the 
 * provided `plantName`. If no name is provided, it logs an error and returns an empty array.
 * 
 * @param {string} plantName - The name of the plant to search for.
 * @returns {Promise<any[]>} - A promise that resolves to an array of plants matching the search name.
 */
export async function getPlantByName(plantName: string) {
    const name = plantName;

    if (!name || name === '') {
        console.log("there was no name for search provided");
        return []
    }

    const response = await MyAPI.get(`/Plant/search/${name}`);
    return response.data;
}

/**
 * Fetches a plant by its ID.
 * 
 * This function makes a request to search for a plant by its unique ID. If the ID is invalid
 * (less than or equal to 0), it logs an error and returns an empty array.
 * 
 * @param {number} ID - The ID of the plant to search for.
 * @returns {Promise<any[]>} - A promise that resolves to an array containing the plant data.
 */
export async function getPlantByID(Id: number) {
    if (!Id || Id <= 0) {
        console.log("Invalid ID provided for search");
        return [];
    }
    const response = await MyAPI.get(`/Plant/${Id}`);
    return response.data;
}


/**
 * Fetches tasks associated with a specific plant.
 * 
 * This function makes a request to get tasks related to the plant identified by `plantId`.
 * If the request is successful, it returns the associated task data; otherwise, it logs an error.
 * 
 * @param {number} plantId - The ID of the plant whose tasks are to be fetched.
 * @returns {Promise<any[]>} - A promise that resolves to an array of tasks related to the plant.
 * @throws {Error} - Throws an error if the task retrieval request fails.
 */
export async function getTasksByPlant(plantId: number) {
    try {
        const response = await MyAPI.get(`/Task/plant/${plantId}`);
        return response.data.data; // The API should return the match data
    } catch (error) {
        console.error("Error fetching plant matches:", error);
        throw error; // Propagate the error so it can be handled elsewhere
    }
}