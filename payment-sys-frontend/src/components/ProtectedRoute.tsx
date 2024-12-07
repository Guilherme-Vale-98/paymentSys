import React, { ReactNode } from "react";
import { useEffect, useState } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useUser } from "../context/UserProvider";

interface ProtectedRouteProps {
    children: ReactNode;
  }

  const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
    const { user } = useUser(); 
    const location = useLocation();
    if (!user) {
        return <Navigate to="/login" state={{ from: location }} />;
    }


    return <>{children}</>;
    return children;
  };
  
  export default ProtectedRoute;