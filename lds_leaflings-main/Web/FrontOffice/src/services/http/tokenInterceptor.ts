import axios from "axios"
import { useNavigate } from "react-router-dom";
import { MyLocalStorage } from "../myLocalStorage"


/**
 * Custom axios instance with interceptors for request handling.
 * 
 * This instance automatically attaches the Authorization token (if available)
 * to every outgoing request and handles 401 Unauthorized errors by redirecting
 * to the login page.
 */
const MyAPI = axios.create({
    baseURL: import.meta.env.VITE_API_URL
})


/**
 * Request interceptor for adding the Authorization token to the headers.
 * 
 * If the `authToken` is found in localStorage, it will be included as a Bearer token
 * in the Authorization header of the request.
 */
MyAPI.interceptors.request.use(
    (config) => {
        const authToken = MyLocalStorage.getItem("authToken")

        if (authToken) {
            config.headers["Authorization"] = `Bearer ${authToken}`
        }

        return config
    },
    (error) => {
        // If the error status is 401 (Unauthorized), will redirect to the login page
        if (error.response && error.response.status === 401) {
            const navigate = useNavigate()

            localStorage.removeItem("authKey");
            navigate("login")
        }

        return Promise.reject(error)
    }
)

export default MyAPI