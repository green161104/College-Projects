import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { routes } from "./AppRoutes";
import "./index.css";
import { RouterProvider } from "react-router-dom";
import { Bounce, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';



/**
 * Initializes and renders the root React application.
 * 
 * Components:
 * - ToastContainer: Displays notifications at the bottom-center with a bounce animation.
 * - RouterProvider: Manages navigation based on the `routes` configuration.
 * 
 * ToastContainer Props:
 * - position: Determines where the toast notifications appear.
 * - autoClose: Sets the duration (in ms) before the toast automatically closes.
 * - hideProgressBar: Indicates whether the progress bar should be displayed.
 * - theme: Defines the visual theme of the toasts (e.g., "colored").
 * - transition: Specifies the animation effect used for displaying toasts.
 */

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ToastContainer
      position="bottom-center"
      autoClose={5000}
      hideProgressBar={false}
      newestOnTop={false}
      closeOnClick
      rtl={false}
      pauseOnFocusLoss
      draggable
      pauseOnHover
      theme="colored"
      transition={Bounce}
   
    />
    <RouterProvider router={routes}></RouterProvider>
  </StrictMode>
);
