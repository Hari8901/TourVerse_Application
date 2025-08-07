import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const Tours = () => {
  const [tours] = useState([1, 2, 3, 4, 5, 6]); // Mock data for now

  return (
    <div className="container py-5">
      <h1 className="text-center mb-5">Popular Tours</h1>
      <div className="row g-4">
        {tours.map(tour => (
          <div key={tour} className="col-md-6 col-lg-4">
            <div className="card feature-card">
              <div className="card-img-top bg-secondary" style={{height: '200px', backgroundColor: '#6c757d'}}></div>
              <div className="card-body">
                <h5 className="card-title">Amazing Tour {tour}</h5>
                <p className="card-text">Experience the beauty and culture of this incredible destination.</p>
                <div className="d-flex justify-content-between align-items-center">
                  <span className="text-primary fw-bold">$999</span>
                  <button className="btn btn-primary btn-sm">View Details</button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Tours;
