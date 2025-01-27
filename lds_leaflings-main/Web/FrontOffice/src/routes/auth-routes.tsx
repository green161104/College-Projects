import LoginPage from "../pages/Login/LoginPage";
import RegisterPage from "../pages/Register/RegisterPage";
import UnauthGuard from "../services/guards/unauthGuard";


/**
 * Defines the authentication-related routes in the application.
 * 
 * These routes are for the login and registration pages and are wrapped with 
 * the `UnauthGuard` component to ensure that only unauthenticated users can 
 * access them. If a user is already authenticated, they will be redirected.
 * 
 * @constant {Array} authRoutes - The list of authentication-related routes.
 * @property {string} path - The URL path for the route.
 * @property {JSX.Element} element - The component to render for the route.
 */
export const authRoutes = [
  {
    path: "/login",
    element: (
      <UnauthGuard >
        <LoginPage/>
      </UnauthGuard>
    ),
  },
  {
    path: "/register",
    element: (
      <UnauthGuard>
        <RegisterPage/>
      </UnauthGuard>
    ),
  },
];
