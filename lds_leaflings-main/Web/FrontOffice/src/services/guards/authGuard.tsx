import { ReactNode } from "react";
import { useAuth } from "../../hooks/useAuth";
import { Navigate } from "react-router-dom";

interface AuthGuardProps {
  /**
   * The child components to render if the user is authenticated.
   */
  children: ReactNode;
}


/**
 * A component that wraps child components and ensures that the user is authenticated before rendering them.
 * 
 * If the user is not authenticated, they will be redirected to the login page (`/login`).
 * If the user is authenticated, the child components are rendered.
 * 
 * @component
 * @example
 * // Example usage of AuthGuard
 * <AuthGuard>
 *   <DashboardPage />
 * </AuthGuard>
 * 
 * @param {AuthGuardProps} props - The properties passed to the AuthGuard component.
 * @param {ReactNode} props.children - The child components to render if authenticated.
 * 
 * @returns {JSX.Element} - Either the child components if authenticated, or a redirect to the login page.
 */
export default function AuthGuard({ children }: AuthGuardProps) {
  const isAuthenticated = useAuth(); // Get authentication status from context

  if (!isAuthenticated) {
    return <Navigate to={`/login`} />;
  }

  return <>{children}</>;
}
