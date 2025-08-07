import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toursAPI, destinationsAPI } from '../services/api';

const Home = () => {
  const [featuredTours, setFeaturedTours] = useState([]);
  const [popularDestinations, setPopularDestinations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchHomeData();
  }, []);

  const fetchHomeData = async () => {
    try {
      const [toursResponse, destinationsResponse] = await Promise.all([
        toursAPI.getFeaturedTours(),
        destinationsAPI.getPopularDestinations()
      ]);
      
      setFeaturedTours(toursResponse.data || []);
      setPopularDestinations(destinationsResponse.data || []);
    } catch (error) {
      console.error('Error fetching home data:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* Hero Section */}
      <section className="hero-section">
        <div className="container">
          <h1 className="display-4 mb-4">Welcome to TourVerse</h1>
          <p className="lead mb-4">Discover incredible destinations and create memories that last a lifetime</p>
          <Link to="/tours" className="btn btn-primary btn-lg">Start Your Journey</Link>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-5">
        <div className="container">
          <h2 className="text-center mb-5">Why Choose TourVerse?</h2>
          <div className="row g-4">
            <div className="col-md-4">
              <div className="card feature-card h-100">
                <div className="card-body text-center">
                  <div className="mb-3">
                    <i className="bi bi-globe2 text-primary" style={{fontSize: '3rem'}}></i>
                  </div>
                  <h5 className="card-title">Global Destinations</h5>
                  <p className="card-text">Explore amazing destinations across the globe with our carefully curated tour packages.</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card feature-card h-100">
                <div className="card-body text-center">
                  <div className="mb-3">
                    <i className="bi bi-people text-primary" style={{fontSize: '3rem'}}></i>
                  </div>
                  <h5 className="card-title">Expert Guides</h5>
                  <p className="card-text">Professional local guides who know the best spots and hidden gems in every destination.</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card feature-card h-100">
                <div className="card-body text-center">
                  <div className="mb-3">
                    <i className="bi bi-shield-check text-primary" style={{fontSize: '3rem'}}></i>
                  </div>
                  <h5 className="card-title">Safe & Secure</h5>
                  <p className="card-text">Your safety is our priority. All tours are designed with comprehensive safety measures.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Tours Section */}
      <section className="py-5 bg-light">
        <div className="container">
          <h2 className="text-center mb-5">Featured Tours</h2>
          {loading ? (
            <div className="text-center">
              <div className="spinner-border text-primary" role="status"></div>
            </div>
          ) : (
            <div className="row g-4">
              {featuredTours.slice(0, 6).map(tour => (
                <div key={tour.id} className="col-md-6 col-lg-4">
                  <div className="card feature-card">
                    <img 
                      src={tour.imageUrl || '/api/placeholder/300/200'} 
                      className="card-img-top" 
                      alt={tour.title}
                      style={{height: '200px', objectFit: 'cover'}}
                    />
                    <div className="card-body">
                      <h5 className="card-title">{tour.title}</h5>
                      <p className="card-text">{tour.description}</p>
                      <div className="d-flex justify-content-between align-items-center">
                        <span className="text-primary fw-bold">${tour.price}</span>
                        <Link to={`/tours/${tour.id}`} className="btn btn-primary btn-sm">
                          View Details
                        </Link>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
          <div className="text-center mt-4">
            <Link to="/tours" className="btn btn-outline-primary">View All Tours</Link>
          </div>
        </div>
      </section>
    </>
  );
};

export default Home;
