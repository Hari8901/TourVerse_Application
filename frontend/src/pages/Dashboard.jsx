import React, { useEffect, useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate, Link } from 'react-router-dom';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [dashboardLoading, setDashboardLoading] = useState(true);

  useEffect(() => {
    // Simulate loading dashboard data
    const timer = setTimeout(() => {
      setDashboardLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const handleStatCardClick = (statType) => {
    console.log(`Clicked on ${statType}`);
    // Add navigation or modal logic here
    switch(statType) {
      case 'countries':
        navigate('/my-travels');
        break;
      case 'trips':
        navigate('/my-trips');
        break;
      case 'reviews':
        navigate('/reviews');
        break;
      case 'savings':
        navigate('/bookings'); // Navigate to bookings to see financial info
        break;
      default:
        break;
    }
  };

  const handleQuickAction = (action) => {
    switch(action) {
      case 'find-tours':
        navigate('/');
        break;
      case 'bookmarks':
        navigate('/favorites');
        break;
      case 'message-guide':
        navigate('/dashboard'); // For now, stay on dashboard
        break;
      default:
        break;
    }
  };

  if (dashboardLoading) {
    return (
      <div className="dashboard-loading">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-md-6 text-center">
              <div className="spinner-border text-primary mb-3" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <h5>Loading your dashboard...</h5>
              <p className="text-muted">Please wait while we prepare your travel insights.</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      {/* Navigation */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary" style={{ position: 'relative', zIndex: 1050 }}>
        <div className="container">
          <Link className="navbar-brand" to="/">
            <i className="bi bi-compass me-2"></i>
            TourVerse
          </Link>
          
          <div className="navbar-nav ms-auto">
            <div className="nav-item dropdown">
              <a
                className="nav-link dropdown-toggle d-flex align-items-center"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                style={{ textDecoration: 'none' }}
              >
                <img
                  src={user?.profilePictureUrl || 'https://via.placeholder.com/32x32'}
                  alt="Profile"
                  className="rounded-circle me-2"
                  width="32"
                  height="32"
                />
                {user?.name?.split(' ')[0] || 'User'}
              </a>
              <ul className="dropdown-menu dropdown-menu-end" style={{ zIndex: 1051 }}>
                <li>
                  <Link className="dropdown-item" to="/profile">
                    <i className="bi bi-person me-2"></i>Edit Profile
                  </Link>
                </li>
                <li>
                  <Link className="dropdown-item" to="/settings">
                    <i className="bi bi-gear me-2"></i>Settings
                  </Link>
                </li>
                <li><hr className="dropdown-divider" /></li>
                <li>
                  <button className="dropdown-item text-danger" onClick={handleLogout}>
                    <i className="bi bi-box-arrow-right me-2"></i>Logout
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container mt-4">
        <div className="row">
          <div className="col-12">
            {/* Welcome Section */}
            <div className="welcome-section mb-4">
              <div className="row align-items-center">
                <div className="col-md-8">
                  <h1 className="display-5 fw-bold">
                    Welcome back, {user?.name?.split(' ')[0] || 'Traveler'}! 👋
                  </h1>
                  <p className="lead text-muted">
                    Ready for your next adventure? Let's explore the world together.
                  </p>
                </div>
                <div className="col-md-4 text-end">
                  <button className="btn btn-primary btn-lg">
                    <i className="bi bi-plus-circle me-2"></i>
                    Plan New Trip
                  </button>
                </div>
              </div>
            </div>

            {/* Quick Stats */}
            <div className="row mb-4">
              <div className="col-md-3">
                <div className="stat-card clickable" onClick={() => handleStatCardClick('countries')}>
                  <div className="stat-icon">
                    <i className="bi bi-map"></i>
                  </div>
                  <div className="stat-content">
                    <h3>12</h3>
                    <p>Countries Visited</p>
                  </div>
                </div>
              </div>
              <div className="col-md-3">
                <div className="stat-card clickable" onClick={() => handleStatCardClick('trips')}>
                  <div className="stat-icon">
                    <i className="bi bi-calendar-check"></i>
                  </div>
                  <div className="stat-content">
                    <h3>5</h3>
                    <p>Trips Planned</p>
                  </div>
                </div>
              </div>
              <div className="col-md-3">
                <div className="stat-card clickable" onClick={() => handleStatCardClick('reviews')}>
                  <div className="stat-icon">
                    <i className="bi bi-star-fill"></i>
                  </div>
                  <div className="stat-content">
                    <h3>4.8</h3>
                    <p>Average Rating</p>
                  </div>
                </div>
              </div>
              <div className="col-md-3">
                <div className="stat-card clickable" onClick={() => handleStatCardClick('savings')}>
                  <div className="stat-icon">
                    <i className="bi bi-currency-dollar"></i>
                  </div>
                  <div className="stat-content">
                    <h3>$2,340</h3>
                    <p>Money Saved</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Recent Activity */}
            <div className="row">
              <div className="col-md-8">
                <div className="card">
                  <div className="card-header">
                    <h5 className="card-title mb-0">
                      <i className="bi bi-clock-history me-2"></i>
                      Recent Activity
                    </h5>
                  </div>
                  <div className="card-body">
                    <div className="activity-item">
                      <div className="activity-icon">
                        <i className="bi bi-bookmark-plus"></i>
                      </div>
                      <div className="activity-content">
                        <h6>Saved "Tokyo Cherry Blossom Tour"</h6>
                        <small className="text-muted">2 hours ago</small>
                      </div>
                    </div>
                    <div className="activity-item">
                      <div className="activity-icon">
                        <i className="bi bi-star"></i>
                      </div>
                      <div className="activity-content">
                        <h6>Reviewed "Paris City Break"</h6>
                        <small className="text-muted">1 day ago</small>
                      </div>
                    </div>
                    <div className="activity-item">
                      <div className="activity-icon">
                        <i className="bi bi-calendar-plus"></i>
                      </div>
                      <div className="activity-content">
                        <h6>Booked "Santorini Sunset Cruise"</h6>
                        <small className="text-muted">3 days ago</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className="col-md-4">
                <div className="card">
                  <div className="card-header">
                    <h5 className="card-title mb-0">
                      <i className="bi bi-person-circle me-2"></i>
                      Profile Summary
                    </h5>
                  </div>
                  <div className="card-body text-center">
                    <img
                      src={user?.profilePictureUrl || 'https://via.placeholder.com/80x80'}
                      alt="Profile"
                      className="rounded-circle mb-3"
                      width="80"
                      height="80"
                    />
                    <h6>{user?.name || 'User Name'}</h6>
                    <p className="text-muted small">{user?.email || 'user@example.com'}</p>
                    <p className="text-muted small">
                      <i className="bi bi-telephone me-1"></i>
                      {user?.phone || 'Phone not provided'}
                    </p>
                    <Link to="/profile" className="btn btn-outline-primary btn-sm">
                      <i className="bi bi-pencil me-1"></i>
                      Edit Profile
                    </Link>
                  </div>
                </div>

                <div className="card mt-3">
                  <div className="card-header">
                    <h5 className="card-title mb-0">
                      <i className="bi bi-lightning me-2"></i>
                      Quick Actions
                    </h5>
                  </div>
                  <div className="card-body">
                    <div className="d-grid gap-2">
                      <button 
                        className="btn btn-outline-primary"
                        onClick={() => handleQuickAction('find-tours')}
                      >
                        <i className="bi bi-search me-2"></i>
                        Find Tours
                      </button>
                      <button 
                        className="btn btn-outline-success"
                        onClick={() => handleQuickAction('bookmarks')}
                      >
                        <i className="bi bi-bookmark me-2"></i>
                        My Bookmarks
                      </button>
                      <button 
                        className="btn btn-outline-info"
                        onClick={() => handleQuickAction('message-guide')}
                      >
                        <i className="bi bi-chat-dots me-2"></i>
                        Message Guide
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
