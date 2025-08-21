import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

// Component for protecting routes that require authentication
const ProtectedRoute = ({ children, requireAuth = true }) => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  // Show loading spinner while checking authentication
  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <div className="mt-2">
            <small className="text-muted">Checking authentication...</small>
          </div>
        </div>
      </div>
    );
  }

  if (requireAuth) {
    // If route requires authentication and user is not authenticated
    if (!isAuthenticated) {
      // Redirect to login page with return URL
      return <Navigate to="/login" state={{ from: location }} replace />;
    }
  } else {
    // If route is for non-authenticated users (like login, register) and user is authenticated
    if (isAuthenticated) {
      // Redirect to dashboard or home
      return <Navigate to="/dashboard" replace />;
    }
  }

  return children;
};

export default ProtectedRoute;
