import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './Navigation.css';

const Navigation = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { user, logout } = useAuth();
  const location = useLocation();

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 50);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const handleLogout = async () => {
    await logout();
    setIsMobileMenuOpen(false);
  };

  const navItems = [
    { href: '/', label: 'Home' },
    { href: '#tours', label: 'Tours' },
    { href: '#destinations', label: 'Destinations' },
    { href: '#about', label: 'About' },
    { href: '#contact', label: 'Contact' }
  ];

  // Don't show navigation on authentication pages
  if (location.pathname.includes('/login') || 
      location.pathname.includes('/register') || 
      location.pathname.includes('/verify-otp') || 
      location.pathname.includes('/forgot-password') || 
      location.pathname.includes('/reset-password') ||
      location.pathname.includes('/dashboard')) {
    return null;
  }

  return (
    <nav className={`navbar navbar-expand-lg fixed-top ${isScrolled ? 'navbar-scrolled' : ''}`}>
      <div className="container">
        {/* Brand */}
        <Link className="navbar-brand" to="/">
          <i className="bi bi-compass me-2"></i>
          <span className="brand-text">TourVerse</span>
        </Link>

        {/* Mobile menu button */}
        <button
          className="navbar-toggler"
          type="button"
          onClick={toggleMobileMenu}
          aria-expanded={isMobileMenuOpen}
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon">
            <i className={`bi ${isMobileMenuOpen ? 'bi-x' : 'bi-list'}`}></i>
          </span>
        </button>

        {/* Navigation items */}
        <div className={`collapse navbar-collapse ${isMobileMenuOpen ? 'show' : ''}`}>
          <ul className="navbar-nav mx-auto">
            {navItems.map((item, index) => (
              <li key={index} className="nav-item">
                {item.href.startsWith('#') ? (
                  <a 
                    className="nav-link" 
                    href={item.href}
                    onClick={() => setIsMobileMenuOpen(false)}
                  >
                    {item.label}
                  </a>
                ) : (
                  <Link 
                    className="nav-link" 
                    to={item.href}
                    onClick={() => setIsMobileMenuOpen(false)}
                  >
                    {item.label}
                  </Link>
                )}
              </li>
            ))}
          </ul>

          {/* Action buttons */}
          <div className="navbar-actions">
            {user ? (
              <div className="nav-item dropdown">
                <a
                  className="nav-link dropdown-toggle d-flex align-items-center"
                  href="#"
                  role="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  <img
                    src={user.profilePictureUrl || 'https://via.placeholder.com/32x32'}
                    alt="Profile"
                    className="rounded-circle me-2"
                    width="32"
                    height="32"
                  />
                  {user.name || 'User'}
                </a>
                <ul className="dropdown-menu">
                  <li>
                    <Link className="dropdown-item" to="/dashboard">
                      <i className="bi bi-speedometer2 me-2"></i>Dashboard
                    </Link>
                  </li>
                  <li>
                    <a className="dropdown-item" href="#profile">
                      <i className="bi bi-person me-2"></i>Profile
                    </a>
                  </li>
                  <li>
                    <a className="dropdown-item" href="#bookings">
                      <i className="bi bi-calendar-check me-2"></i>My Bookings
                    </a>
                  </li>
                  <li><hr className="dropdown-divider" /></li>
                  <li>
                    <button className="dropdown-item" onClick={handleLogout}>
                      <i className="bi bi-box-arrow-right me-2"></i>Logout
                    </button>
                  </li>
                </ul>
              </div>
            ) : (
              <>
                <Link to="/login" className="btn btn-outline-primary me-2">
                  <i className="bi bi-person me-1"></i>
                  Sign In
                </Link>
                <Link to="/register" className="btn btn-primary">
                  <i className="bi bi-plus-circle me-1"></i>
                  Get Started
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navigation;
