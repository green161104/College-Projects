import { addRefreshAuthToAuthProvider } from "react-admin";

/**
 * Checks if an auth token exists in local storage.
 * @returns {Promise<void>} Resolves if the token exists, rejects otherwise.
 */
export const authToken = () => {
    const authToken = localStorage.getItem('authToken');

    if (authToken) {
        return Promise.resolve();
    }

    return Promise.reject();
}

// Base API URL loaded from environment variables.
const url = import.meta.env.VITE_API_URL || "";

/**
 * Interface representing login credentials.
 */
interface AuthProps {
    email: string; // User's email address.
    password: string; // User's password.
}

/**
 * Interface representing an error response.
 */
interface ErrorResponse {
    status: number; // HTTP status code of the error.
    statusText: string; // HTTP status text of the error.
}

/**
 * Custom authentication provider for React Admin.
 */
const myAuthProvider = {
    /**
     * Logs the user in by sending credentials to the server.
     * @param {AuthProps} param0 - An object containing the user's email and password.
     * @returns {Promise<void>} Resolves if login is successful, rejects otherwise.
     */
    login: ({ email, password }: AuthProps) => {
        const request = new Request(`${url}/Auth/login`, {
            method: 'POST',
            body: JSON.stringify({ email, password }),
            headers: new Headers({ 'Content-Type': 'application/json' }),
        });
        
        return fetch(request)
            .then(response => {
                if (response.status < 200 || response.status >= 300) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(auth => {
                localStorage.setItem('authToken', auth.token);
                localStorage.setItem('userId', auth.userId);
            })
            .catch(() => {
                throw new Error('Network error');
            });
    },

    /**
     * Checks for authentication errors and removes the token if unauthorized.
     * @param {ErrorResponse} error - The error response from the server.
     * @returns {Promise<void>} Rejects if the error is related to authorization, resolves otherwise.
     */
    checkError: (error: ErrorResponse) => {
        const status = error.status;
        if (status === 401 || status === 403) {
            localStorage.removeItem('authToken');
            return Promise.reject();
        }

        return Promise.resolve();
    },

    /**
     * Verifies whether the user is authenticated.
     * @returns {Promise<void>} Resolves if an auth token exists, rejects otherwise.
     */
    checkAuth: () => localStorage.getItem('authToken')
        ? Promise.resolve()
        : Promise.reject(),

    /**
     * Retrieves user permissions. Currently resolves with no specific permissions.
     * @returns {Promise<void>} Resolves with no permissions.
     */
    getPermissions: () => Promise.resolve(),

    /**
     * Logs the user out by removing the auth token.
     * @returns {Promise<void>} Resolves once the token is removed.
     */
    logout: () => {
        localStorage.removeItem('authToken');
        return Promise.resolve();
    }
};

/**
 * Auth provider enhanced with token refresh functionality.
 */
export const authProvider = addRefreshAuthToAuthProvider(myAuthProvider, authToken);
