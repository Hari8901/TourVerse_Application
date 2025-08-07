import React from 'react';

const Destinations = () => {
  const destinations = ['Paris', 'Tokyo', 'New York', 'London', 'Rome', 'Barcelona'];

  return (
    <div className="container py-5">
      <h1 className="text-center mb-5">Top Destinations</h1>
      <div className="row g-4">
        {destinations.map(destination => (
          <div key={destination} className="col-md-6 col-lg-4">
            <div className="card feature-card">
              <div className="card-img-top bg-info" style={{height: '250px'}}></div>
              <div className="card-body">
                <h5 className="card-title">{destination}</h5>
                <p className="card-text">Discover the wonders of {destination} with our exclusive tour packages.</p>
                <button className="btn btn-outline-primary">Explore {destination}</button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Destinations;
