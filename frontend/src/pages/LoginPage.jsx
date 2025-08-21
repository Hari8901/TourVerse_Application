import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const LoginPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { 
    loginInit, 
    loginVerify, 
    loading, 
    error, 
    otpStage, 
    tempEmail, 
    clearError,
    isAuthenticated 
  } = useAuth();

  const [formData, setFormData] = useState({
    email: '',
    password: '',
    otp: ''
  });
  
  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      const from = location.state?.from?.pathname || '/dashboard';
      navigate(from, { replace: true });
    }
  }, [isAuthenticated, navigate, location]);

  // Clear error when component mounts or form changes
  useEffect(() => {
    clearError();
    setSuccessMessage(location.state?.message || '');
  }, [clearError, location.state]);

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear field-specific error
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // Form validation
  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }
    
    if (!otpStage && !formData.password.trim()) {
      newErrors.password = 'Password is required';
    }
    
    if (otpStage && !formData.otp.trim()) {
      newErrors.otp = 'OTP is required';
    } else if (otpStage && formData.otp.length !== 6) {
      newErrors.otp = 'OTP must be 6 digits';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    try {
      if (!otpStage) {
        // Step 1: Login initiation
        const result = await loginInit({
          email: formData.email,
          password: formData.password
        });
        
        if (result.success) {
          setSuccessMessage('OTP sent to your email. Please check your inbox.');
          setFormData(prev => ({ ...prev, password: '' })); // Clear password for security
        }
      } else {
        // Step 2: OTP verification
        const result = await loginVerify(tempEmail || formData.email, formData.otp);
        
        if (result.success) {
          setSuccessMessage('Login successful! Redirecting...');
          setTimeout(() => {
            const from = location.state?.from?.pathname || '/dashboard';
            navigate(from, { replace: true });
          }, 1000);
        }
      }
    } catch (err) {
      console.error('Login error:', err);
    }
  };

  // Handle back to credentials
  const handleBackToCredentials = () => {
    setFormData({
      email: tempEmail || formData.email,
      password: '',
      otp: ''
    });
    clearError();
  };

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="auth-card">
              <div className="auth-header">
                <h2 className="auth-title">
                  <i className="bi bi-person-circle me-2"></i>
                  {otpStage ? 'Verify OTP' : 'Welcome Back'}
                </h2>
                <p className="auth-subtitle">
                  {otpStage 
                    ? `We've sent a 6-digit code to ${tempEmail || formData.email}`
                    : 'Sign in to your TourVerse account'
                  }
                </p>
              </div>

              <div className="auth-body">
                {/* Success Message */}
                {successMessage && (
                  <div className="alert alert-success" role="alert">
                    <i className="bi bi-check-circle me-2"></i>
                    {successMessage}
                  </div>
                )}

                {/* Error Message */}
                {error && (
                  <div className="alert alert-danger" role="alert">
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    {error}
                  </div>
                )}

                <form onSubmit={handleSubmit} className="auth-form">
                  {/* Email Field */}
                  <div className="mb-3">
                    <label className="form-label">
                      <i className="bi bi-envelope me-2"></i>
                      Email Address
                    </label>
                    <input
                      type="email"
                      className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      placeholder="Enter your email"
                      disabled={otpStage}
                      required
                    />
                    {errors.email && (
                      <div className="invalid-feedback">{errors.email}</div>
                    )}
                  </div>

                  {/* Password Field - Only show if not in OTP stage */}
                  {!otpStage && (
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-lock me-2"></i>
                        Password
                      </label>
                      <div className="password-input-group">
                        <input
                          type={showPassword ? 'text' : 'password'}
                          className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                          name="password"
                          value={formData.password}
                          onChange={handleChange}
                          placeholder="Enter your password"
                          required
                        />
                        <button
                          type="button"
                          className="password-toggle"
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          <i className={`bi ${showPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                        </button>
                      </div>
                      {errors.password && (
                        <div className="invalid-feedback">{errors.password}</div>
                      )}
                    </div>
                  )}

                  {/* OTP Field - Only show if in OTP stage */}
                  {otpStage && (
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-shield-check me-2"></i>
                        Enter OTP
                      </label>
                      <input
                        type="text"
                        className={`form-control otp-input ${errors.otp ? 'is-invalid' : ''}`}
                        name="otp"
                        value={formData.otp}
                        onChange={handleChange}
                        placeholder="000000"
                        maxLength="6"
                        autoComplete="off"
                        required
                      />
                      {errors.otp && (
                        <div className="invalid-feedback">{errors.otp}</div>
                      )}
                      <div className="form-text">
                        <i className="bi bi-info-circle me-1"></i>
                        Didn't receive the code? Check your spam folder.
                      </div>
                    </div>
                  )}

                  {/* Submit Button */}
                  <button
                    type="submit"
                    className="btn btn-primary w-100 auth-submit-btn"
                    disabled={loading}
                  >
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2"></span>
                        {otpStage ? 'Verifying...' : 'Signing In...'}
                      </>
                    ) : (
                      <>
                        <i className={`bi ${otpStage ? 'bi-check-circle' : 'bi-box-arrow-in-right'} me-2`}></i>
                        {otpStage ? 'Verify OTP' : 'Sign In'}
                      </>
                    )}
                  </button>

                  {/* Back to credentials button for OTP stage */}
                  {otpStage && (
                    <button
                      type="button"
                      className="btn btn-outline-secondary w-100 mt-2"
                      onClick={handleBackToCredentials}
                    >
                      <i className="bi bi-arrow-left me-2"></i>
                      Back to Credentials
                    </button>
                  )}
                </form>

                {/* Additional Links - Only show if not in OTP stage */}
                {!otpStage && (
                  <>
                    <div className="text-center mt-3">
                      <Link to="/forgot-password" className="forgot-password-link">
                        <i className="bi bi-question-circle me-1"></i>
                        Forgot Password?
                      </Link>
                    </div>

                    <div className="auth-divider">
                      <span>New to TourVerse?</span>
                    </div>

                    <div className="text-center">
                      <Link to='/register' className="btn btn-outline-primary w-100">
                        <i className="bi bi-person-plus me-2"></i>
                        Create Account
                      </Link>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
