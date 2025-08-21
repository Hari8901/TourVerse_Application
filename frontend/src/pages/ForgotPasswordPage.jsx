import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const ForgotPasswordPage = () => {
  const navigate = useNavigate();
  const { forgotPassword, loading, error, clearError, isAuthenticated } = useAuth();

  const [email, setEmail] = useState('');
  const [emailError, setEmailError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Clear error when component mounts
  useEffect(() => {
    clearError();
  }, [clearError]);

  // Handle email change
  const handleEmailChange = (e) => {
    setEmail(e.target.value);
    if (emailError) {
      setEmailError('');
    }
  };

  // Validate email
  const validateEmail = () => {
    if (!email.trim()) {
      setEmailError('Email is required');
      return false;
    }
    
    if (!/\S+@\S+\.\S+/.test(email)) {
      setEmailError('Please enter a valid email address');
      return false;
    }
    
    return true;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateEmail()) return;
    
    try {
      const result = await forgotPassword(email);
      
      if (result.success) {
        setSuccessMessage(result.message);
        setIsSubmitted(true);
        
        // Redirect to reset password page after a delay
        setTimeout(() => {
          navigate('/reset-password', { 
            state: { 
              email,
              message: 'OTP sent to your email. Please check your inbox.' 
            }
          });
        }, 3000);
      }
    } catch (err) {
      console.error('Forgot password error:', err);
    }
  };

  // Handle try again
  const handleTryAgain = () => {
    setIsSubmitted(false);
    setSuccessMessage('');
    setEmail('');
    clearError();
  };

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="auth-card">
              <div className="auth-header text-center">
                <div className="forgot-password-icon mb-3">
                  <i className="bi bi-key"></i>
                </div>
                <h2 className="auth-title">
                  {isSubmitted ? 'Check Your Email' : 'Forgot Password?'}
                </h2>
                <p className="auth-subtitle">
                  {isSubmitted 
                    ? `We've sent password reset instructions to ${email}`
                    : 'No worries! Enter your email and we\'ll send you reset instructions.'
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

                {!isSubmitted ? (
                  <form onSubmit={handleSubmit} className="auth-form">
                    {/* Email Input */}
                    <div className="mb-4">
                      <label className="form-label">
                        <i className="bi bi-envelope me-2"></i>
                        Email Address
                      </label>
                      <input
                        type="email"
                        className={`form-control ${emailError ? 'is-invalid' : ''}`}
                        value={email}
                        onChange={handleEmailChange}
                        placeholder="Enter your email address"
                        autoComplete="email"
                        required
                      />
                      {emailError && (
                        <div className="invalid-feedback">{emailError}</div>
                      )}
                      <div className="form-text">
                        <i className="bi bi-info-circle me-1"></i>
                        We'll send password reset instructions to this email.
                      </div>
                    </div>

                    {/* Submit Button */}
                    <button
                      type="submit"
                      className="btn btn-primary w-100 auth-submit-btn"
                      disabled={loading}
                    >
                      {loading ? (
                        <>
                          <span className="spinner-border spinner-border-sm me-2"></span>
                          Sending Instructions...
                        </>
                      ) : (
                        <>
                          <i className="bi bi-send me-2"></i>
                          Send Reset Instructions
                        </>
                      )}
                    </button>
                  </form>
                ) : (
                  <div className="text-center">
                    {/* Success State */}
                    <div className="success-state mb-4">
                      <div className="success-icon mb-3">
                        <i className="bi bi-check-circle-fill"></i>
                      </div>
                      <h4 className="mb-3">Instructions Sent!</h4>
                      <p className="text-muted mb-4">
                        Check your email and follow the instructions to reset your password. 
                        If you don't see it, check your spam folder.
                      </p>
                      
                      {/* Action Buttons */}
                      <div className="d-grid gap-2">
                        <Link to="/reset-password" 
                              state={{ email, message: 'OTP sent to your email.' }}
                              className="btn btn-primary">
                          <i className="bi bi-arrow-right me-2"></i>
                          Continue to Reset Password
                        </Link>
                        
                        <button
                          type="button"
                          className="btn btn-outline-secondary"
                          onClick={handleTryAgain}
                        >
                          <i className="bi bi-arrow-left me-2"></i>
                          Try Another Email
                        </button>
                      </div>
                    </div>
                  </div>
                )}

                {/* Back to Login */}
                {!isSubmitted && (
                  <>
                    <div className="auth-divider">
                      <span>Remember your password?</span>
                    </div>

                    <div className="text-center">
                      <Link to="/login" className="btn btn-outline-primary w-100">
                        <i className="bi bi-arrow-left me-2"></i>
                        Back to Login
                      </Link>
                    </div>
                  </>
                )}

                {/* Additional Help */}
                <div className="text-center mt-4">
                  <small className="text-muted">
                    <i className="bi bi-question-circle me-1"></i>
                    Still having trouble? <Link to="/contact">Contact Support</Link>
                  </small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ForgotPasswordPage;
