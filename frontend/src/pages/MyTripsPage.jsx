import React from 'react';
import { Link } from 'react-router-dom';
import './AuthPages.css';

const MyTripsPage = () => {
  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-10 col-lg-8">
            <div className="auth-card">
              <div className="auth-header">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <h2 className="auth-title">
                      <i className="bi bi-calendar-check me-2"></i>
                      My Trips
                    </h2>
                    <p className="auth-subtitle">
                      Manage your upcoming and past trips
                    </p>
                  </div>
                  <Link to="/dashboard" className="btn btn-outline-secondary">
                    <i className="bi bi-arrow-left me-1"></i>
                    Back to Dashboard
                  </Link>
                </div>
              </div>

              <div className="auth-body">
                <div className="text-center py-5">
                  <div className="mb-4">
                    <i className="bi bi-suitcase-lg" style={{ fontSize: '4rem', color: '#6c757d' }}></i>
                  </div>
                  <h4 className="text-muted mb-3">No trips planned</h4>
                  <p className="text-muted mb-4">
                    Plan your next adventure! Your upcoming and past trips will be displayed here.
                  </p>
                  <Link to="/" className="btn btn-primary btn-lg">
                    <i className="bi bi-plus-circle me-2"></i>
                    Plan a Trip
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyTripsPage;
