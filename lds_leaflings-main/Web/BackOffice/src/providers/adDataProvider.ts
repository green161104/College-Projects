import { CreateParams, DataProvider, fetchUtils, GetOneResult } from 'react-admin';
import { Ad } from '../models/ad-model';

// Define the base URL for the API
const API_URL = import.meta.env.VITE_API_URL;
const SERVER_URL = import.meta.env.VITE_SERVER_URL;

/**
 * Data provider for managing ads.
 * This object is responsible for handling CRUD operations for ads.
 */
export const adDataProvider: DataProvider = {
    /**
     * Fetches a paginated list of ads with optional sorting.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params Pagination and sorting parameters.
     * @param {Object} params.pagination Pagination settings (page and perPage).
     * @param {Object} params.sort Sorting configuration (field and order).
     * 
     * @returns {Promise<{ data: Ad[], total: number }>} A promise that resolves to the paginated list of ads and the total count.
     */
    getList: async (resource, params) => {
        const { page, perPage } = params.pagination || { page: 1, perPage: 10 };
        let { field } = params.sort || { field: 'id', order: 'ASC' };
        const { order } = params.sort || { field: 'id', order: 'ASC' };
        const authToken = localStorage.getItem('authToken');

        // IsActive is boolean so it cannot be sorted
        if(field == "isActive") {
            field = "Id"
        }

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

        // Add the server URL to the adImage field
        const jsonData = response.json.data;
        const data = jsonData.map(addServerUrlToAdImage);

        return {
            data: data,
            total: response.json.total,
        };
    },

    /**
     * Fetches a single ad by ID.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params Contains the ID of the ad to fetch.
     * @param {number} params.id The ID of the ad.
     * 
     * @returns {Promise<{ data: Ad }>} A promise that resolves to the fetched ad data.
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

        // Add the server URL to the adImage field
        const jsonData = response.json;
        const data = addServerUrlToAdImage(jsonData);

        return {
            data: data,
        } as GetOneResult;
    },

    /**
     * Fetches multiple ads by their IDs.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params Contains the IDs of the ads to fetch.
     * @param {number[]} params.ids The array of ad IDs to fetch.
     * 
     * @returns {Promise<{ data: Ad[] }>} A promise that resolves to the list of ads.
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

        // Add the server URL to the adImage field
        const jsonData = response.json.data;
        const data = jsonData.map(addServerUrlToAdImage);

        return {
            data: data,
        };
    },

    /**
     * Fetches a list of ads filtered by a reference field.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params Pagination and reference parameters.
     * @param {Object} params.pagination Pagination settings (page and perPage).
     * @param {Object} params.sort Sorting configuration (field and order).
     * @param {string} params.target The target field for filtering (e.g., "adminId").
     * @param {string|number} params.id The ID to filter ads by.
     * 
     * @returns {Promise<{ data: Ad[], total: number }>} A promise that resolves to the filtered list of ads.
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

        // Add the server URL to the adImage field
        const jsonData = response.json.data;
        const data = jsonData.map(addServerUrlToAdImage);

        return {
            data: data,
            total: response.json.total,
        };
    },

    /**
     * Updates an existing ad.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params The parameters for updating the ad.
     * @param {number} params.id The ID of the ad to update.
     * @param {Object} params.data The data to update the ad with.
     * 
     * @returns {Promise<{ data: Ad }>} A promise that resolves to the updated ad data.
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

        return {
            data: response.json,
        };
    },

    /**
     * Updates multiple ads.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params The parameters for updating multiple ads.
     * @param {number[]} params.ids The IDs of the ads to update.
     * @param {Object} params.data The data to update the ads with.
     * 
     * @returns {Promise<{ data: Ad[] }>} A promise that resolves to the updated ads data.
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

            responses.push(response.json);
        }

        return {
            data: responses,
        };
    },

    /**
     * Creates a new ad.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params The parameters for creating the ad.
     * @param {Object} params.data The data for the new ad.
     * 
     * @returns {Promise<{ data: Ad }>} A promise that resolves to the newly created ad data.
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

        return {
            data: response.json,
        };
    },

    /**
     * Deletes a specific ad by ID.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params The parameters for deleting the ad.
     * @param {number} params.id The ID of the ad to delete.
     * 
     * @returns {Promise<{ data: Ad }>} A promise that resolves to the deleted ad data.
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
     * Deletes multiple ads.
     * 
     * @param {string} resource The resource name (e.g., "ads").
     * @param {Object} params The parameters for deleting multiple ads.
     * @param {number[]} params.ids The IDs of the ads to delete.
     * 
     * @returns {Promise<{ data: Ad[] }>} A promise that resolves to the deleted ads data.
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
 * Helper function to create form data for ad creation or update.
 * 
 * @param {CreateParams} params The parameters containing the ad data.
 * 
 * @returns {FormData} The constructed FormData object.
 */
function createPostFormData(params: CreateParams) {
    const formData = new FormData();

    for (const key in params.data) {
        const value = params.data[key];
        if (key === 'adFile' && value) {
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
 * Helper function to add the server URL to the adImage field of an ad.
 * 
 * @param {Ad} ad The ad object to modify.
 * 
 * @returns {Ad} The modified ad object with the updated adFile field.
 */
function addServerUrlToAdImage(ad: Ad): Ad {
    const adImage = ad.adFile;
    if (adImage) {
        ad.adFile = `${SERVER_URL}/${adImage}`;
    } else {
        // If the plantImage is null, use the default image from the public folder
        ad.adFile = `${window.location.origin}/images/default_ad_image.jpg`;
    }
    return ad;
}
