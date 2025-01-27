import { CreateParams, DataProvider, fetchUtils, GetOneResult } from 'react-admin';
import { Plant } from '../models/plant-model';
import { Task, TaskRequest } from '../models/task-model';

// Define the base URL for the API
const API_URL = import.meta.env.VITE_API_URL;
const SERVER_URL = import.meta.env.VITE_SERVER_URL

/**
 * A React Admin Data Provider for managing plants and their associated tasks.
 * Handles CRUD operations for plants and tasks, integrates file uploads, and includes authentication.
 */
export const plantDataProvider: DataProvider = {
     /**
     * Fetches a paginated list of plants with tasks and additional data transformations.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - The parameters including pagination, sorting, and filtering.
     * @returns {Promise<Object>} A promise resolving to an object containing the data and total count.
     */
    getList: async (resource, params) => {
        const { page, perPage } = params.pagination || { page: 1, perPage: 10 };
        const { field, order } = params.sort || { field: 'id', order: 'ASC' };
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}?_page=${page}&_limit=${perPage}&_sort=${field}&_order=${order}`,
            {
                method: 'GET',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
            }
        );

        if (response.status === 204) {
            return {
                data: [],
                total: 0,
            };
        }

        // Add the server URL to the plantImage field
        const jsonData = response.json.data
        const data = jsonData.map(addServerUrlToPlantImage);

        // Add Task data in Plant
        const dataWithTasks = await Promise.all(
            data.map(async (plant: Plant) => {
                const tasks = await getTasks(plant.id);
                return {
                    ...plant,
                    tasks
                };
            })
        );

        return {
            data: dataWithTasks,
            total: response.json.total,
        };
    },

      /**
     * Fetches a single plant by ID, including associated tasks.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - The parameters including the plant ID.
     * @returns {Promise<GetOneResult>} A promise resolving to the plant data.
     */
    getOne: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}/${params.id}`,
            {
                method: 'GET',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
            }
        );

        // Add the server URL to the plantImage field
        const jsonData = response.json
        const data = addServerUrlToPlantImage(jsonData);

        // Add tasks information in plant
        const tasks = await getTasks(data.id)
        const dataWithTasks = {
            ...data,
            tasks
        }

        return {
            data: dataWithTasks,
        } as GetOneResult;
    },

     /**
     * Fetches multiple plants by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - The parameters including an array of IDs.
     * @returns {Promise<Object>} A promise resolving to the fetched plants.
     */
    getMany: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}?id=${params.ids}`,
            {
                method: 'GET',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
            }
        );

        // Add the server URL to the plantImage field
        const jsonData = response.json.data
        const data = jsonData.map(addServerUrlToPlantImage);

        // Add Task data in Plant
        const dataWithTasks = await Promise.all(
            data.map(async (plant: Plant) => {
                const tasks = await getTasks(plant.id);
                return {
                    ...plant,
                    tasks
                };
            })
        );

        return {
            data: dataWithTasks,
        };
    },

       /**
     * Fetches plants related to another resource (e.g., tasks).
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including target, ID, pagination, and sorting.
     * @returns {Promise<Object>} A promise resolving to related plants.
     */
    getManyReference: async (resource, params) => {
        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}?${params.target}=${params.id}&_page=${page}&_limit=${perPage}&_sort=${field}&_order=${order}`,
            {
                method: 'GET',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
            }
        );

        // Add the server URL to the plantImage field
        const jsonData = response.json.data
        const data = jsonData.map(addServerUrlToPlantImage);

        // Add Task data in Plant
        const dataWithTasks = await Promise.all(
            data.map(async (plant: Plant) => {
                const tasks = await getTasks(plant.id);
                return {
                    ...plant,
                    tasks
                };
            })
        );

        return {
            data: dataWithTasks,
            total: response.json.total,
        };
    },

    /**
     * Updates an existing plant, including associated tasks.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including the plant ID and updated data.
     * @returns {Promise<Object>} A promise resolving to the updated plant data.
     */
    update: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const formData = createPostFormData(params);

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}/${params.id}`,
            {
                method: 'PUT',
                headers: new Headers({
                    'Authorization': `Bearer ${authToken}`
                }),
                body: formData,
            }
        );

        // Update tasks
        const updatedPlantId = params.id;
        const tasks = params.data.tasks;
        await updateTasks(tasks, updatedPlantId);

        return {
            data: response.json,
        };
    },

    /**
     * Updates multiple plants by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including an array of IDs and updated data.
     * @returns {Promise<Object>} A promise resolving to the updated plants.
     */
    updateMany: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const responses = [];
        const ids = params.ids;
        for (const id of ids) {
            const formData = createPostFormData(params);

            const response = await fetchUtils.fetchJson(
                `${API_URL}/${resource}/${id}`,
                {
                    method: 'PATCH',
                    headers: new Headers({
                        'Authorization': `Bearer ${authToken}`
                    }),
                    body: formData,
                }
            );

            // Update tasks
            const tasks = params.data.tasks;
            const idInt = Number.parseInt(id.toString());
            await updateTasks(tasks, idInt);

            responses.push(response.json);
        }

        return {
            data: responses,
        };
    },

    /**
     * Creates a new plant, including associated tasks.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including the plant data.
     * @returns {Promise<Object>} A promise resolving to the created plant data.
     */
    create: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const formData = createPostFormData(params);

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}`,
            {
                method: 'POST',
                headers: new Headers({
                    'Authorization': `Bearer ${authToken}`
                }),
                body: formData,
            }
        );

        const insertedPlantId = response.json.id;
        
        const tasks = params.data.tasks;
        insertTasks(tasks, insertedPlantId);

        return {
            data: response.json,
        };
    },

    /**
     * Deletes a plant by its ID.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including the plant ID.
     * @returns {Promise<Object>} A promise resolving to the deleted plant data.
     */
    delete: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}/${params.id}`,
            {
                method: 'DELETE',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
            }
        );

        return {
            data: response.json,
        };
    },

    /**
     * Deletes multiple plants by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'plants').
     * @param {Object} params - Parameters including an array of IDs.
     * @returns {Promise<Object>} A promise resolving to the deleted plants.
     */
    deleteMany: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const responses = [];
        const ids = params.ids;
        for (const id of ids) {
            const response = await fetchUtils.fetchJson(
                `${API_URL}/${resource}/${id}`,
                {
                    method: 'DELETE',
                    headers: new Headers({
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}`
                    }),
                    body: JSON.stringify(params),
                }
            );

            responses.push(response.json);
        }

        return {
            data: responses,
        };
    }
}

/**
 * Creates a FormData object for a plant, handling file uploads and additional fields.
 *
 * @param {CreateParams} params - Parameters including the plant data.
 * @returns {FormData} The constructed FormData object.
 */
function createPostFormData(params: CreateParams): FormData {
    const formData = new FormData();

    for (const key in params.data) {
        const value = params.data[key];
        if (key === 'plantImage' && value) {
            formData.append(key, value.rawFile);
            continue;
        }

        formData.append(key, value);
    }

    // Add the adminId to the formData
    const adminId = localStorage.getItem('userId');
    if (adminId) {
        formData.append('adminId', adminId);
    }

    return formData;
};


/**
 * Adds the server URL to the plant image path or sets a default image if none exists.
 *
 * @param {Plant} plant - The plant object.
 * @returns {Plant} The updated plant with the full image URL.
 */
function addServerUrlToPlantImage(plant: Plant): Plant {
    const plantImage = plant.plantImage;
    if (plantImage) {
        plant.plantImage = `${SERVER_URL}/${plantImage}`;
    }
    else {
        // If the plantImage is null, use the default image from the public folder
        plant.plantImage = `${window.location.origin}/images/default_plant_image.jpg`;
    }
    return plant;
}


/**
 * Inserts tasks associated with a plant into the database.
 *
 * @param {TaskRequest[]} tasks - The tasks to be inserted.
 * @param {number} insertedPlantId - The ID of the associated plant.
 */
async function insertTasks(tasks: TaskRequest[], insertedPlantId: number) {
    tasks.forEach(async (task) => {
        const newTask = {
            ...task,
            plantId: insertedPlantId,
            adminId: localStorage.getItem('userId')
        }

        // Insert the task
        await fetchUtils.fetchJson(
            `${API_URL}/Task`,
            {
                method: 'POST',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }),
                body: JSON.stringify(newTask),
            }
        );
    });
}


/**
 * Retrieves tasks associated with a specific plant.
 *
 * @param {number} plantId - The ID of the plant.
 * @returns {Promise<Task[]>} A promise resolving to the tasks for the plant.
 */
async function getTasks(plantId: number) {
    const response = await fetch(
        `${API_URL}/Task/plant/${plantId}`,
        {
            method: 'GET',
            headers: new Headers({
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }),
        }
    );

    if (response.status == 404) {
        return []
    }

    const data = await response.json()
    return data.data

}


/**
 * Updates tasks for a specific plant. It handles updates, creation of new tasks, and deletion of removed tasks.
 *
 * @param {Task[]} tasks - The tasks to be updated or created.
 * @param {number} plantId - The ID of the associated plant.
 */
async function updateTasks(tasks: Task[], plantId: number) {
    const existingTasks = await getTasks(plantId);
    const existingTaskIds = existingTasks.map((task: Task) => task.id);

    // Update or create tasks
    for (const task of tasks) {
        if (task.id && existingTaskIds.includes(task.id)) {
            // Update existing task
            await fetchUtils.fetchJson(
                `${API_URL}/Task/${task.id}`,
                {
                    method: 'PUT',
                    headers: new Headers({
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                    }),
                    body: JSON.stringify(task),
                }
            );
        } else {
            // Create new task
            const newTask = {
                ...task,
                plantId,
                adminId: localStorage.getItem('userId')
            };
            await fetchUtils.fetchJson(
                `${API_URL}/Task`,
                {
                    method: 'POST',
                    headers: new Headers({
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                    }),
                    body: JSON.stringify(newTask),
                }
            );
        }
    }

    // Delete removed tasks
    const newTaskIds = tasks.map(task => task.id).filter(id => id);
    const tasksToDelete = existingTaskIds.filter((id: number) => !newTaskIds.includes(id));
    for (const taskId of tasksToDelete) {
        await fetchUtils.fetchJson(
            `${API_URL}/Task/${taskId}`,
            {
                method: 'DELETE',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }),
            }
        );
    }
}