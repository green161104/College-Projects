import { DataProvider, fetchUtils } from 'react-admin';

// Define the base URL for the API
const API_URL = import.meta.env.VITE_API_URL;

export const userDataProvider: DataProvider = {
     /**
     * Fetches a paginated list of users.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including pagination, sorting, and filtering.
     * @returns {Promise<Object>} A promise resolving to an object containing the data and total count.
     */
    getList: async (resource, params) => {
        const { page, perPage } = params.pagination || { page: 1, perPage: 10 };
        let { field } = params.sort || { field: 'id' };
        const { order } = params.sort || { order: 'ASC' };
        const authToken = localStorage.getItem('authToken');

        // rolePaid is boolean so it cannot be sorted (This only applies for User)
        if(field == "rolePaid") {
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

        // If returns no Content, return empty data
        if (response.status === 204) {
            return {
                data: [],
                total: 0,
            };
        }
        
        return {
            data: response.json.data,
            total: response.json.total
        };
    },

        /**
     * Fetches a single user by ID.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including the user ID.
     * @returns {Promise<Object>} A promise resolving to the user data.
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
     * Fetches multiple users by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including an array of user IDs.
     * @returns {Promise<Object>} A promise resolving to the fetched users.
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
     * Fetches users related to another resource (e.g., user roles).
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including the related resource and pagination details.
     * @returns {Promise<Object>} A promise resolving to related users.
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
     * Updates an existing user by ID.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including the user ID and updated data.
     * @returns {Promise<Object>} A promise resolving to the updated user data.
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
     * Updates multiple users by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including an array of user IDs and updated data.
     * @returns {Promise<Object>} A promise resolving to the updated users.
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
     * Creates a new user.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including the new user data.
     * @returns {Promise<Object>} A promise resolving to the created user data.
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
     * Deletes a user by ID.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including the user ID.
     * @returns {Promise<Object>} A promise resolving to the deleted user data.
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
     * Deletes multiple users by their IDs.
     *
     * @param {string} resource - The resource name (e.g., 'users').
     * @param {Object} params - Parameters including an array of user IDs.
     * @returns {Promise<Object>} A promise resolving to the deleted users.
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