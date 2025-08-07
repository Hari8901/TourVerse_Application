import React from 'react';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <div className="container py-5">
      <div className="text-center">
        <h1 className="display-1">404</h1>
        <h2 className="mb-4">Page Not Found</h2>
        <p className="lead mb-4">
          Sorry, the page you are looking for doesn't exist or has been moved.
        </p>
        <Link to="/" className="btn btn-primary">
          Go Back Home
        </Link>
      </div>
    </div>
  );
};

export default NotFoundPage;
