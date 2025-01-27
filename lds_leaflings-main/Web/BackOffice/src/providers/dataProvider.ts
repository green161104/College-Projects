import { combineDataProviders } from 'react-admin';
import { plantDataProvider } from './plantDataProvider';
import { defaultDataProvider } from './defaultDataProvider';
import { adDataProvider } from './adDataProvider';
import { userDataProvider } from './userDataProvider';

// Create an HTTP client that adds the authToken to each request
// const httpClient = (url: string, options: any = {}) => {
//     if (!options.headers) {
//         options.headers = new Headers({ Accept: 'application/json' });

//     }

//     // Add the Authorization header with the authToken
//     const authToken = localStorage.getItem('authToken');
//     if (authToken) {
//         options.headers.set('Authorization', `Bearer ${authToken}`);
//     }

//     return fetchUtils.fetchJson(url, options);
// };

/**
 * Combines multiple data providers based on the resource type.
 * This function returns the appropriate data provider based on the given resource.
 * 
 * @param {string} resource - The name of the resource (e.g., 'Plant', 'Ad', 'User').
 * @returns {DataProvider} - The corresponding data provider for the resource.
 * 
 * @example
 * const plantProvider = dataProvider('Plant');
 * const adProvider = dataProvider('Ad');
 * const userProvider = dataProvider('User');
 */
export const dataProvider = combineDataProviders((resource) => {
    switch (resource) {
        case 'Plant':
            return plantDataProvider;
        case 'Ad':
            return adDataProvider;
        case 'User':
            return userDataProvider;
        default:
            return defaultDataProvider;
    }
});
