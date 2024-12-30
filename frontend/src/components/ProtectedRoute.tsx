import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

interface ProtectedRouteProps {
  children: React.ReactNode;
  roles?: Array<"STUDENT" | "TEACHER" | "ADMIN">;
}

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  /*   const { isAuthenticated, user } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/auth/login" state={{ from: location }} replace />;
  }

  if (roles && user && !roles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  } */

  return <>{children}</>;
};

export default ProtectedRoute;
