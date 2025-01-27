import { DataProvider, fetchUtils } from 'react-admin';

// Define the base URL for the API
const API_URL = import.meta.env.VITE_API_URL;

/**
 * Default DataProvider implementation for handling CRUD operations.
 * This DataProvider interacts with the backend API using RESTful endpoints.
 */
export const defaultDataProvider: DataProvider = {
    /**
     * Fetch a list of resources with optional pagination and sorting.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for pagination and sorting.
     * @param {Object} params.pagination - Pagination settings (page, perPage).
     * @param {Object} params.sort - Sorting settings (field, order).
     * @returns {Promise<Object>} - The fetched data and total count.
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

        return {
            data: response.json.data,
            total: response.json.total,
        };
    },

    /**
     * Fetch a single resource by ID.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {string|number} params.id - The ID of the resource to fetch.
     * @returns {Promise<Object>} - The fetched resource.
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

        return {
            data: response.json,
        };
    },

    /**
     * Fetch multiple resources by IDs.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {Array<string|number>} params.ids - The IDs of the resources to fetch.
     * @returns {Promise<Object>} - The fetched resources.
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

        return {
            data: response.json,
        };
    },

    /**
     * Fetch a list of related resources based on a reference.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {string|number} params.id - The ID of the reference.
     * @param {string} params.target - The reference field name.
     * @param {Object} params.pagination - Pagination settings.
     * @param {Object} params.sort - Sorting settings.
     * @returns {Promise<Object>} - The fetched related resources.
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

        return {
            data: response.json.data,
            total: response.json.total,
        };
    },

    /**
     * Update a single resource by ID.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {string|number} params.id - The ID of the resource to update.
     * @param {Object} params.data - The data to update.
     * @returns {Promise<Object>} - The updated resource.
     */
    update: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}/${params.id}`,
            {
                method: 'PUT',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
                body: JSON.stringify(params.data),
            }
        );

        return {
            data: response.json,
        };
    },

    /**
     * Update multiple resources by their IDs.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {Array<string|number>} params.ids - The IDs of the resources to update.
     * @param {Object} params.data - The data to update for all resources.
     * @returns {Promise<Object>} - The updated resources.
     */
    updateMany: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const responses = [];
        const ids = params.ids;
        for(const id of ids) {
            const response = await fetchUtils.fetchJson(
                `${API_URL}/${resource}/${id}`,
                {
                    method: 'PATCH',
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
    },

    /**
     * Create a new resource.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {Object} params.data - The data for the new resource.
     * @returns {Promise<Object>} - The created resource.
     */
    create: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const response = await fetchUtils.fetchJson(
            `${API_URL}/${resource}`,
            {
                method: 'POST',
                headers: new Headers({
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                }),
                body: JSON.stringify(params.data),
            }
        );

        return {
            data: response.json,
        };
    },

    /**
     * Delete a single resource by ID.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {string|number} params.id - The ID of the resource to delete.
     * @returns {Promise<Object>} - The deleted resource.
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
     * Delete multiple resources by their IDs.
     * 
     * @param {string} resource - The resource name.
     * @param {Object} params - Parameters for the request.
     * @param {Array<string|number>} params.ids - The IDs of the resources to delete.
     * @returns {Promise<Object>} - The deleted resources.
     */
    deleteMany: async (resource, params) => {
        const authToken = localStorage.getItem('authToken');

        const responses = [];
        const ids = params.ids;
        for(const id of ids) {
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
};
