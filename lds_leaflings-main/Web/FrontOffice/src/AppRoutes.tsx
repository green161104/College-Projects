import { createBrowserRouter } from "react-router-dom";

import NotFoundPage from "./pages/404/NotFoundPage.tsx";
import HomePage from "./pages/Root.tsx";
import { authRoutes } from "./routes/auth-routes.tsx";
import { userRoutes } from "./routes/user-routes.tsx";


/**
 * The application's route configuration for the router.
 * 
 * This configuration defines all the paths and associated components for the app, 
 * including the home page, authentication routes, and user routes.
 * It also specifies a custom error page (`NotFoundPage`) to handle undefined routes.
 * 
 * @constant {BrowserRouter} routes - The routes configuration for the application.
 */
export const routes = createBrowserRouter([
  {
    path: "/",
    element: <HomePage />,
    errorElement: <NotFoundPage />,
  },
  ...authRoutes, // all authentication routes
  ...userRoutes
]);
