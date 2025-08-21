import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const RegisterPage = () => {
  const navigate = useNavigate();
  const { 
    registerInit, 
    registerVerify, 
    loading, 
    error, 
    otpStage, 
    tempEmail, 
    clearError,
    isAuthenticated 
  } = useAuth();

  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    profilePicture: null,
    otp: ''
  });
  
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [profilePreview, setProfilePreview] = useState(null);

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

  // Update step based on OTP stage
  useEffect(() => {
    if (otpStage) {
      setCurrentStep(2);
    }
  }, [otpStage]);

  // Handle input changes
  const handleChange = (e) => {
    const { name, value, files } = e.target;
    
    if (name === 'profilePicture' && files[0]) {
      const file = files[0];
      setFormData(prev => ({
        ...prev,
        [name]: file
      }));
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e) => setProfilePreview(e.target.result);
      reader.readAsDataURL(file);
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
    
    // Clear field-specific error
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // Form validation for Step 1
  const validateStep1 = () => {
    const newErrors = {};
    
    if (!formData.name.trim()) {
      newErrors.name = 'Full name is required';
    } else if (formData.name.trim().length < 2) {
      newErrors.name = 'Name must be at least 2 characters';
    }
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }
    
    if (!formData.phone.trim()) {
      newErrors.phone = 'Phone number is required';
    } else if (!/^\+?[\d\s-()]+$/.test(formData.phone)) {
      newErrors.phone = 'Please enter a valid phone number';
    }
    
    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters';
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(formData.password)) {
      newErrors.password = 'Password must contain at least one uppercase letter, one lowercase letter, and one number';
    }
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Form validation for Step 2 (OTP)
  const validateStep2 = () => {
    const newErrors = {};
    
    if (!formData.otp.trim()) {
      newErrors.otp = 'OTP is required';
    } else if (formData.otp.length !== 6) {
      newErrors.otp = 'OTP must be 6 digits';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle Step 1 submission (Registration initiation)
  const handleStep1Submit = async (e) => {
    e.preventDefault();
    
    if (!validateStep1()) return;
    
    try {
      const result = await registerInit(formData);
      
      if (result.success) {
        setSuccessMessage('OTP sent to your email. Please check your inbox.');
        setCurrentStep(2);
      }
    } catch (err) {
      console.error('Registration error:', err);
    }
  };

  // Handle Step 2 submission (OTP verification)
  const handleStep2Submit = async (e) => {
    e.preventDefault();
    
    if (!validateStep2()) return;
    
    try {
      const result = await registerVerify(tempEmail || formData.email, formData.otp, formData);
      
      if (result.success) {
        setSuccessMessage('Registration successful! Redirecting to login...');
        setTimeout(() => {
          navigate('/login', { 
            state: { message: 'Registration successful! Please login to continue.' }
          });
        }, 2000);
      }
    } catch (err) {
      console.error('OTP verification error:', err);
    }
  };

  // Handle back to step 1
  const handleBackToStep1 = () => {
    setCurrentStep(1);
    setFormData(prev => ({ ...prev, otp: '' }));
    clearError();
  };

  // Remove profile picture
  const removeProfilePicture = () => {
    setFormData(prev => ({ ...prev, profilePicture: null }));
    setProfilePreview(null);
  };

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-8 col-lg-6">
            <div className="auth-card">
              <div className="auth-header">
                <h2 className="auth-title">
                  <i className="bi bi-person-plus-fill me-2"></i>
                  {currentStep === 1 ? 'Create Account' : 'Verify Your Email'}
                </h2>
                <p className="auth-subtitle">
                  {currentStep === 1 
                    ? 'Join TourVerse and start your travel journey'
                    : `We've sent a 6-digit code to ${tempEmail || formData.email}`
                  }
                </p>
              </div>

              {/* Progress indicator */}
              <div className="registration-progress mb-4">
                <div className="progress">
                  <div 
                    className="progress-bar" 
                    style={{ width: `${currentStep * 50}%` }}
                  ></div>
                </div>
                <div className="progress-steps">
                  <div className={`step ${currentStep >= 1 ? 'active' : ''}`}>
                    <i className="bi bi-person"></i>
                    <span>Details</span>
                  </div>
                  <div className={`step ${currentStep >= 2 ? 'active' : ''}`}>
                    <i className="bi bi-envelope-check"></i>
                    <span>Verify</span>
                  </div>
                </div>
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

                {/* Step 1: Registration Form */}
                {currentStep === 1 && (
                  <form onSubmit={handleStep1Submit} className="auth-form">
                    {/* Profile Picture Upload */}
                    <div className="mb-4 text-center">
                      <div className="profile-upload">
                        {profilePreview ? (
                          <div className="profile-preview">
                            <img src={profilePreview} alt="Profile preview" />
                            <button
                              type="button"
                              className="btn btn-sm btn-danger remove-photo"
                              onClick={removeProfilePicture}
                            >
                              <i className="bi bi-x"></i>
                            </button>
                          </div>
                        ) : (
                          <div className="profile-placeholder">
                            <i className="bi bi-person-circle"></i>
                            <span>Add Photo</span>
                          </div>
                        )}
                        <input
                          type="file"
                          id="profilePicture"
                          name="profilePicture"
                          onChange={handleChange}
                          accept="image/*"
                          className="d-none"
                        />
                        <label htmlFor="profilePicture" className="btn btn-outline-primary btn-sm mt-2">
                          <i className="bi bi-camera me-1"></i>
                          {profilePreview ? 'Change Photo' : 'Add Photo'}
                        </label>
                      </div>
                    </div>

                    {/* Full Name */}
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-person me-2"></i>
                        Full Name
                      </label>
                      <input
                        type="text"
                        className={`form-control ${errors.name ? 'is-invalid' : ''}`}
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="Enter your full name"
                        required
                      />
                      {errors.name && (
                        <div className="invalid-feedback">{errors.name}</div>
                      )}
                    </div>

                    {/* Email */}
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
                        required
                      />
                      {errors.email && (
                        <div className="invalid-feedback">{errors.email}</div>
                      )}
                    </div>

                    {/* Phone */}
                    <div className="mb-3">
                      <label className="form-label">
                        <i className="bi bi-telephone me-2"></i>
                        Phone Number
                      </label>
                      <input
                        type="tel"
                        className={`form-control ${errors.phone ? 'is-invalid' : ''}`}
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        placeholder="Enter your phone number"
                        required
                      />
                      {errors.phone && (
                        <div className="invalid-feedback">{errors.phone}</div>
                      )}
                    </div>

                    {/* Password */}
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
                          placeholder="Create a strong password"
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
                      <div className="form-text">
                        <i className="bi bi-info-circle me-1"></i>
                        Password must be at least 8 characters with uppercase, lowercase, and number.
                      </div>
                    </div>

                    {/* Confirm Password */}
                    <div className="mb-4">
                      <label className="form-label">
                        <i className="bi bi-lock-fill me-2"></i>
                        Confirm Password
                      </label>
                      <div className="password-input-group">
                        <input
                          type={showConfirmPassword ? 'text' : 'password'}
                          className={`form-control ${errors.confirmPassword ? 'is-invalid' : ''}`}
                          name="confirmPassword"
                          value={formData.confirmPassword}
                          onChange={handleChange}
                          placeholder="Confirm your password"
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
                          Creating Account...
                        </>
                      ) : (
                        <>
                          <i className="bi bi-arrow-right me-2"></i>
                          Create Account
                        </>
                      )}
                    </button>
                  </form>
                )}

                {/* Step 2: OTP Verification */}
                {currentStep === 2 && (
                  <form onSubmit={handleStep2Submit} className="auth-form">
                    <div className="mb-4 text-center">
                      <div className="otp-icon">
                        <i className="bi bi-envelope-check"></i>
                      </div>
                    </div>

                    {/* OTP Field */}
                    <div className="mb-4">
                      <label className="form-label">
                        <i className="bi bi-shield-check me-2"></i>
                        Enter Verification Code
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

                    {/* Submit Button */}
                    <button
                      type="submit"
                      className="btn btn-primary w-100 auth-submit-btn"
                      disabled={loading}
                    >
                      {loading ? (
                        <>
                          <span className="spinner-border spinner-border-sm me-2"></span>
                          Verifying...
                        </>
                      ) : (
                        <>
                          <i className="bi bi-check-circle me-2"></i>
                          Verify & Complete Registration
                        </>
                      )}
                    </button>

                    {/* Back Button */}
                    <button
                      type="button"
                      className="btn btn-outline-secondary w-100 mt-2"
                      onClick={handleBackToStep1}
                    >
                      <i className="bi bi-arrow-left me-2"></i>
                      Back to Registration Form
                    </button>
                  </form>
                )}

                {/* Login Link - Only show in step 1 */}
                {currentStep === 1 && (
                  <>
                    <div className="auth-divider">
                      <span>Already have an account?</span>
                    </div>

                    <div className="text-center">
                      <Link to="/login" className="btn btn-outline-primary w-100">
                        <i className="bi bi-box-arrow-in-right me-2"></i>
                        Sign In Instead
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

export default RegisterPage;
