import MyPlantDiaryPage from "../pages/Diary/MyPlantDiaryPage";
import MyGardenPage from "../pages/MyGarden/MyGardenPage";
import PaymentPage from "../pages/Payment/PaymentPage";
import DashboardPage from "../pages/UserDashboard/DashboardPage";
import ProfilePage from "../pages/UserProfile/ProfilePage";
import AuthGuard from "../services/guards/authGuard";


/**
 * Defines the user-related routes in the application.
 * 
 * These routes are for pages that can only be accessed by authenticated users, such as
 * the user dashboard, profile page, premium payment page, garden page, and plant diary page.
 * Each route is protected by the `AuthGuard` component, which ensures that users must be 
 * authenticated to access these pages.
 * 
 * @constant {Array} userRoutes - The list of user-related routes.
 * @property {string} path - The URL path for the route.
 * @property {JSX.Element} element - The component to render for the route.
 */
export const userRoutes = [
  {
    path: "/user/dashboard",
    element: (
      <AuthGuard>
        <DashboardPage />
      </AuthGuard>
    ),
  },
  {
    path: "/user/profile",
    element: (
      <AuthGuard>
        <ProfilePage />
      </AuthGuard>
    ),
  },
  {
    path: "/user/premium",
    element: (
      <AuthGuard>
        <PaymentPage />
      </AuthGuard>
    ),
  },
  {
    path: "/user/mygarden",
    element: (
      <AuthGuard>
        <MyGardenPage />
      </AuthGuard>
    ),
  },
  {
    path: "/user/mygarden/diary/:plantId/:userplantId",
    element: (
      <AuthGuard>
        <MyPlantDiaryPage />
      </AuthGuard>
    ),
  },
];
