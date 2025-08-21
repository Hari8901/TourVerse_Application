import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const ResetPasswordPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { resetPassword, loading, error, clearError, isAuthenticated } = useAuth();

  // Get email from location state or require input
  const emailFromState = location.state?.email;
  const messageFromState = location.state?.message;

  const [formData, setFormData] = useState({
    email: emailFromState || '',
    newPassword: '',
    confirmPassword: '',
    otp: ''
  });
  
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState(messageFromState || '');
  const [isPasswordReset, setIsPasswordReset] = useState(false);

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
    
    if (!formData.otp.trim()) {
      newErrors.otp = 'OTP is required';
    } else if (formData.otp.length !== 6) {
      newErrors.otp = 'OTP must be 6 digits';
    }
    
    if (!formData.newPassword) {
      newErrors.newPassword = 'New password is required';
    } else if (formData.newPassword.length < 8) {
      newErrors.newPassword = 'Password must be at least 8 characters';
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(formData.newPassword)) {
      newErrors.newPassword = 'Password must contain at least one uppercase letter, one lowercase letter, and one number';
    }
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your new password';
    } else if (formData.newPassword !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    try {
      const result = await resetPassword(formData.email, formData.newPassword, formData.otp);
      
      if (result.success) {
        setSuccessMessage(result.message);
        setIsPasswordReset(true);
        
        // Redirect to login after delay
        setTimeout(() => {
          navigate('/login', { 
            state: { 
              message: 'Password reset successful! Please login with your new password.' 
            }
          });
        }, 3000);
      }
    } catch (err) {
      console.error('Reset password error:', err);
    }
  };

  // Handle going back to forgot password
  const handleBackToForgotPassword = () => {
    navigate('/forgot-password');
  };

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="auth-card">
              <div className="auth-header text-center">
                <div className="reset-password-icon mb-3">
                  <i className={`bi ${isPasswordReset ? 'bi-check-circle-fill' : 'bi-shield-lock'}`}></i>
                </div>
                <h2 className="auth-title">
                  {isPasswordReset ? 'Password Reset Complete!' : 'Reset Your Password'}
                </h2>
                <p className="auth-subtitle">
                  {isPasswordReset 
                    ? 'Your password has been successfully reset.'
                    : 'Enter the OTP sent to your email and your new password.'
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

                {!isPasswordReset ? (
                  <form onSubmit={handleSubmit} className="auth-form">
                    {/* Email Input */}
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
                        disabled={!!emailFromState}
                        required
                      />
                      {errors.email && (
                        <div className="invalid-feedback">{errors.email}</div>
                      )}
                    </div>

                    {/* OTP Input */}
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-shield-check me-2"></i>
                        Verification Code
                      </label>
                      <input
                        type="text"
                        className={`form-control otp-input ${errors.otp ? 'is-invalid' : ''}`}
                        name="otp"
                        value={formData.otp}
                        onChange={handleChange}
                        placeholder="Enter 6-digit OTP"
                        maxLength="6"
                        autoComplete="off"
                        required
                      />
                      {errors.otp && (
                        <div className="invalid-feedback">{errors.otp}</div>
                      )}
                      <div className="form-text">
                        <i className="bi bi-info-circle me-1"></i>
                        Check your email for the verification code.
                      </div>
                    </div>

                    {/* New Password */}
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-lock me-2"></i>
                        New Password
                      </label>
                      <div className="password-input-group">
                        <input
                          type={showPassword ? 'text' : 'password'}
                          className={`form-control ${errors.newPassword ? 'is-invalid' : ''}`}
                          name="newPassword"
                          value={formData.newPassword}
                          onChange={handleChange}
                          placeholder="Enter new password"
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
                      {errors.newPassword && (
                        <div className="invalid-feedback">{errors.newPassword}</div>
                      )}
                      <div className="form-text">
                        <i className="bi bi-info-circle me-1"></i>
                        Password must be at least 8 characters with uppercase, lowercase, and number.
                      </div>
                    </div>

                    {/* Confirm New Password */}
                    <div className="mb-4">
                      <label className="form-label">
                        <i className="bi bi-lock-fill me-2"></i>
                        Confirm New Password
                      </label>
                      <div className="password-input-group">
                        <input
                          type={showConfirmPassword ? 'text' : 'password'}
                          className={`form-control ${errors.confirmPassword ? 'is-invalid' : ''}`}
                          name="confirmPassword"
                          value={formData.confirmPassword}
                          onChange={handleChange}
                          placeholder="Confirm new password"
                          required
                        />
                        <button
                          type="button"
                          className="password-toggle"
                          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        >
                          <i className={`bi ${showConfirmPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                        </button>
                      </div>
                      {errors.confirmPassword && (
                        <div className="invalid-feedback">{errors.confirmPassword}</div>
                      )}
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
                          Resetting Password...
                        </>
                      ) : (
                        <>
                          <i className="bi bi-check-circle me-2"></i>
                          Reset Password
                        </>
                      )}
                    </button>

                    {/* Back Button */}
                    <button
                      type="button"
                      className="btn btn-outline-secondary w-100 mt-2"
                      onClick={handleBackToForgotPassword}
                    >
                      <i className="bi bi-arrow-left me-2"></i>
                      Back to Forgot Password
                    </button>
                  </form>
                ) : (
                  // Success State
                  <div className="text-center">
                    <div className="success-state mb-4">
                      <div className="success-icon mb-3">
                        <i className="bi bi-check-circle-fill"></i>
                      </div>
                      <h4 className="mb-3">Password Successfully Reset!</h4>
                      <p className="text-muted mb-4">
                        Your password has been successfully updated. You can now login with your new password.
                      </p>
                      
                      {/* Action Button */}
                      <Link to="/login" className="btn btn-primary w-100">
                        <i className="bi bi-box-arrow-in-right me-2"></i>
                        Continue to Login
                      </Link>
                    </div>
                  </div>
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

export default ResetPasswordPage;
