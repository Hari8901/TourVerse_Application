import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="container">
        <div className="row">
          <div className="col-md-4">
            <h5>TourVerse</h5>
            <p>Discover amazing destinations and create unforgettable memories with our curated travel experiences.</p>
          </div>
          <div className="col-md-4">
            <h5>Quick Links</h5>
            <ul className="list-unstyled">
              <li><Link to="/tours" className="text-light">Popular Tours</Link></li>
              <li><Link to="/destinations" className="text-light">Destinations</Link></li>
              <li><Link to="/about" className="text-light">About Us</Link></li>
              <li><Link to="/contact" className="text-light">Contact</Link></li>
            </ul>
          </div>
          <div className="col-md-4">
            <h5>Contact Info</h5>
            <p><i className="bi bi-envelope"></i> info@tourverse.com</p>
            <p><i className="bi bi-phone"></i> +1 (555) 123-4567</p>
            <p><i className="bi bi-geo-alt"></i> 123 Travel Street, Adventure City</p>
          </div>
        </div>
        <hr className="my-4" />
        <div className="text-center">
          <p>&copy; 2024 TourVerse. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
