import React from 'react';
import { Link } from 'react-router-dom';
import './AuthPages.css';

const BookingsPage = () => {
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
                      <i className="bi bi-receipt me-2"></i>
                      My Bookings
                    </h2>
                    <p className="auth-subtitle">
                      View and manage your booking confirmations
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
                    <i className="bi bi-journal-check" style={{ fontSize: '4rem', color: '#6c757d' }}></i>
                  </div>
                  <h4 className="text-muted mb-3">No bookings found</h4>
                  <p className="text-muted mb-4">
                    Your booking confirmations and receipts will appear here once you make your first reservation.
                  </p>
                  <Link to="/" className="btn btn-primary btn-lg">
                    <i className="bi bi-calendar-plus me-2"></i>
                    Make a Booking
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

export default BookingsPage;
