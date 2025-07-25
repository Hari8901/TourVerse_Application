import React from 'react';

import heroBackground from '../assets/images/header/home.jpg';
// import header1 from '../assets/images/header/header1.jpg';
// import header2 from '../assets/images/header/header2.jpg';
// import header3 from '../assets/images/header/header3.jpg';

import './HomePage.css';

function TourComponent() {
  return (
    <section
      className="d-flex align-items-center text-center text-white"
      style={{
        backgroundImage: `url(${heroBackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        position: 'relative'
      }}
    >
      {/* Overlay */}
      <div style={{
        position: 'absolute',
        top: 0, left: 0, right: 0, bottom: 0,
        backgroundColor: 'rgba(0,0,0,0.4)',
        zIndex: 1
      }}></div>

      <div className="container position-relative" style={{ zIndex: 2, marginTop: '-100px' }}>
        <h1 className="display-3 fw-bold" style={{ fontFamily: "'Pacifico', cursive", color: '#FFD700' }}>
          Find your <span style={{ color: '#FFD700' }}>Next tour!</span>
        </h1>
        <p className="lead mb-4">Where would you like to go?</p>

        {/* Search Box */}
        <form className="row justify-content-center g-2">
          <div className="col-md-4">
            <input type="text" className="form-control" placeholder="When Would you like to go ?" />
          </div>
          <div className="col-md-3">
            <select className="form-select">
              <option>When</option>
              <option>Today</option>
              <option>Next Week</option>
            </select>
          </div>
          <div className="col-md-2">
            <button type="submit" className="btn btn-warning w-100 text-uppercase">Search</button>
          </div>
        </form>
      </div>
    </section>
  );
}

export default TourComponent;
