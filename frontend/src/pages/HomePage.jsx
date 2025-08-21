import { useState, useEffect } from 'react';
import Navigation from '../components/Navigation';
import './HomePage.css';

const HomePage = () => {
  const [searchData, setSearchData] = useState({
    destination: '',
    activity: '',
    date: ''
  });

  const featuredTours = [
    {
      id: 1,
      image: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2000&q=80",
      title: "Santorini Paradise",
      description: "Experience the breathtaking beauty of Santorini with its iconic blue domes and stunning sunsets.",
      price: "$1,299",
      duration: "7 Days"
    },
    {
      id: 2,
      image: "https://images.unsplash.com/photo-1539650116574-75c0c6d73f6e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2000&q=80",
      title: "Swiss Alps Adventure",
      description: "Discover the majestic Swiss Alps with guided hiking tours and mountain adventures.",
      price: "$1,899",
      duration: "10 Days"
    },
    {
      id: 3,
      image: "https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2000&q=80",
      title: "Tropical Bali Retreat",
      description: "Relax in paradise with pristine beaches, cultural experiences, and luxury accommodations.",
      price: "$999",
      duration: "5 Days"
    }
  ];

  const whyChooseUs = [
    {
      icon: "bi-person-check",
      title: "Expert Guides",
      description: "Professional local guides with extensive knowledge and experience"
    },
    {
      icon: "bi-gear",
      title: "Customizable Itineraries",
      description: "Tailor your trip to match your preferences and interests"
    },
    {
      icon: "bi-headset",
      title: "24/7 Support",
      description: "Round-the-clock assistance for all your travel needs"
    },
    {
      icon: "bi-shield-check",
      title: "Safe & Secure",
      description: "Your safety is our priority with comprehensive travel insurance"
    }
  ];

  const testimonials = [
    {
      id: 1,
      name: "Sarah Johnson",
      quote: "TourVerse made our honeymoon unforgettable! The attention to detail and personalized service exceeded all expectations.",
      image: "https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
      rating: 5
    },
    {
      id: 2,
      name: "Mike Chen",
      quote: "Amazing adventure tours! The guides were knowledgeable and the experiences were once-in-a-lifetime.",
      image: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
      rating: 5
    },
    {
      id: 3,
      name: "Emily Rodriguez",
      quote: "Perfectly organized trip with seamless logistics. I'll definitely book with TourVerse again!",
      image: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
      rating: 5
    }
  ];

  const handleSearchChange = (e) => {
    setSearchData({
      ...searchData,
      [e.target.name]: e.target.value
    });
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    console.log('Search data:', searchData);
    // Here you would typically make an API call to search for tours
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  useEffect(() => {
    // Add scroll animations
    const observerOptions = {
      threshold: 0.1,
      rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('animate-in');
        }
      });
    }, observerOptions);

    const animateElements = document.querySelectorAll('.animate-on-scroll');
    animateElements.forEach(el => observer.observe(el));

    return () => observer.disconnect();
  }, []);

  return (
    <div className="homepage">
      {/* Navigation */}
      <Navigation />

      {/* Hero Section */}
      <section id="home" className="hero-section">
        <div className="hero-overlay"></div>
        <div className="hero-content">
          <div className="container">
            <div className="row justify-content-center text-center">
              <div className="col-lg-8">
                <h1 className="hero-title animate-fade-in">
                  Explore the World with <span className="text-primary">TourVerse</span>
                </h1>
                <p className="hero-subtitle animate-fade-in-delay">
                  Discover extraordinary destinations and create unforgettable memories with our expertly crafted travel experiences.
                </p>
                <button className="btn btn-primary btn-lg cta-button animate-fade-in-delay-2">
                  <i className="bi bi-compass me-2"></i>
                  Find Your Next Adventure
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Search Bar Section */}
      <section className="search-section">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-lg-10">
              <div className="search-card animate-on-scroll">
                <form onSubmit={handleSearchSubmit} className="search-form">
                  <div className="row g-3">
                    <div className="col-md-4">
                      <div className="search-input-group">
                        <i className="bi bi-geo-alt search-icon"></i>
                        <input
                          type="text"
                          className="form-control search-input"
                          placeholder="Where do you want to go?"
                          name="destination"
                          value={searchData.destination}
                          onChange={handleSearchChange}
                        />
                      </div>
                    </div>
                    <div className="col-md-3">
                      <div className="search-input-group">
                        <i className="bi bi-activity search-icon"></i>
                        <select
                          className="form-select search-input"
                          name="activity"
                          value={searchData.activity}
                          onChange={handleSearchChange}
                        >
                          <option value="">Activity Type</option>
                          <option value="adventure">Adventure</option>
                          <option value="cultural">Cultural</option>
                          <option value="relaxation">Relaxation</option>
                          <option value="wildlife">Wildlife</option>
                        </select>
                      </div>
                    </div>
                    <div className="col-md-3">
                      <div className="search-input-group">
                        <i className="bi bi-calendar search-icon"></i>
                        <input
                          type="date"
                          className="form-control search-input"
                          name="date"
                          value={searchData.date}
                          onChange={handleSearchChange}
                        />
                      </div>
                    </div>
                    <div className="col-md-2">
                      <button type="submit" className="btn btn-primary btn-search w-100">
                        <i className="bi bi-search"></i>
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Tours Section */}
      <section className="featured-tours-section">
        <div className="container">
          <div className="row">
            <div className="col-12 text-center mb-5">
              <h2 className="section-title animate-on-scroll">Featured Tours</h2>
              <p className="section-subtitle animate-on-scroll">Discover our most popular and extraordinary travel experiences</p>
            </div>
          </div>
          <div className="row g-4">
            {featuredTours.map((tour, index) => (
              <div key={tour.id} className="col-lg-4 col-md-6">
                <div className={`tour-card animate-on-scroll delay-${index + 1}`}>
                  <div className="tour-image-container">
                    <img src={tour.image} alt={tour.title} className="tour-image" />
                    <div className="tour-overlay">
                      <span className="tour-price">{tour.price}</span>
                    </div>
                  </div>
                  <div className="tour-content">
                    <div className="tour-header">
                      <h4 className="tour-title">{tour.title}</h4>
                      <span className="tour-duration">
                        <i className="bi bi-clock me-1"></i>
                        {tour.duration}
                      </span>
                    </div>
                    <p className="tour-description">{tour.description}</p>
                    <button className="btn btn-outline-primary btn-tour">
                      View Details
                      <i className="bi bi-arrow-right ms-2"></i>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Why Choose Us Section */}
      <section className="why-choose-section">
        <div className="container">
          <div className="row">
            <div className="col-12 text-center mb-5">
              <h2 className="section-title animate-on-scroll text-white">Why Choose TourVerse?</h2>
              <p className="section-subtitle animate-on-scroll text-white-50">We're committed to making your travel dreams come true</p>
            </div>
          </div>
          <div className="row g-4">
            {whyChooseUs.map((item, index) => (
              <div key={index} className="col-lg-3 col-md-6">
                <div className={`feature-card animate-on-scroll delay-${index + 1}`}>
                  <div className="feature-icon">
                    <i className={`bi ${item.icon}`}></i>
                  </div>
                  <h4 className="feature-title">{item.title}</h4>
                  <p className="feature-description">{item.description}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="testimonials-section">
        <div className="container">
          <div className="row">
            <div className="col-12 text-center mb-5">
              <h2 className="section-title animate-on-scroll">What Our Travelers Say</h2>
              <p className="section-subtitle animate-on-scroll">Real experiences from real people</p>
            </div>
          </div>
          <div className="row g-4">
            {testimonials.map((testimonial, index) => (
              <div key={testimonial.id} className="col-lg-4 col-md-6">
                <div className={`testimonial-card animate-on-scroll delay-${index + 1}`}>
                  <div className="testimonial-content">
                    <div className="quote-icon">
                      <i className="bi bi-quote"></i>
                    </div>
                    <p className="testimonial-quote">"{testimonial.quote}"</p>
                    <div className="testimonial-rating">
                      {[...Array(testimonial.rating)].map((_, i) => (
                        <i key={i} className="bi bi-star-fill"></i>
                      ))}
                    </div>
                  </div>
                  <div className="testimonial-author">
                    <img src={testimonial.image} alt={testimonial.name} className="author-image" />
                    <div className="author-info">
                      <h5 className="author-name">{testimonial.name}</h5>
                      <p className="author-title">Verified Traveler</p>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="container">
          <div className="row g-4">
            <div className="col-lg-4 col-md-6">
              <div className="footer-section">
                <h3 className="footer-title">
                  <i className="bi bi-compass me-2"></i>
                  TourVerse
                </h3>
                <p className="footer-description">
                  Your trusted partner in creating extraordinary travel experiences. Explore the world with confidence and discover new adventures.
                </p>
                <div className="social-links">
                  <a href="#" className="social-link">
                    <i className="bi bi-facebook"></i>
                  </a>
                  <a href="#" className="social-link">
                    <i className="bi bi-twitter"></i>
                  </a>
                  <a href="#" className="social-link">
                    <i className="bi bi-instagram"></i>
                  </a>
                  <a href="#" className="social-link">
                    <i className="bi bi-youtube"></i>
                  </a>
                </div>
              </div>
            </div>
            <div className="col-lg-2 col-md-6">
              <div className="footer-section">
                <h4 className="footer-section-title">Company</h4>
                <ul className="footer-links">
                  <li><a href="#">About Us</a></li>
                  <li><a href="#">Our Team</a></li>
                  <li><a href="#">Careers</a></li>
                  <li><a href="#">Press</a></li>
                </ul>
              </div>
            </div>
            <div className="col-lg-2 col-md-6">
              <div className="footer-section">
                <h4 className="footer-section-title">Support</h4>
                <ul className="footer-links">
                  <li><a href="#">Contact</a></li>
                  <li><a href="#">Help Center</a></li>
                  <li><a href="#">Safety</a></li>
                  <li><a href="#">Terms of Service</a></li>
                </ul>
              </div>
            </div>
            <div className="col-lg-2 col-md-6">
              <div className="footer-section">
                <h4 className="footer-section-title">Legal</h4>
                <ul className="footer-links">
                  <li><a href="#">Privacy Policy</a></li>
                  <li><a href="#">Cookie Policy</a></li>
                  <li><a href="#">Refund Policy</a></li>
                  <li><a href="#">Disclaimer</a></li>
                </ul>
              </div>
            </div>
            <div className="col-lg-2 col-md-6">
              <div className="footer-section">
                <h4 className="footer-section-title">Destinations</h4>
                <ul className="footer-links">
                  <li><a href="#">Europe</a></li>
                  <li><a href="#">Asia</a></li>
                  <li><a href="#">Americas</a></li>
                  <li><a href="#">Africa</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div className="footer-bottom">
            <div className="row align-items-center">
              <div className="col-md-6">
                <p className="copyright">
                  © 2025 TourVerse. All rights reserved.
                </p>
              </div>
              <div className="col-md-6 text-md-end">
                <button className="btn-scroll-top" onClick={scrollToTop}>
                  <i className="bi bi-arrow-up"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default HomePage;
