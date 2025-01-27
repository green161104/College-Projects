import { Navigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { ReactNode } from "react";

interface UnauthGuardProps {
  children: ReactNode;
}

export default function UnauthGuard({ children }: UnauthGuardProps) {
  const isAuthenticated = useAuth(); // Get authentication status from context

  if (isAuthenticated) {
    // Redirect to a dashboard or home page if the user is already authenticated
    return <Navigate to={`/user/dashboard`} />;
  }

  // If not authenticated, allow access to the requested route
  return <>{children}</>;
}
