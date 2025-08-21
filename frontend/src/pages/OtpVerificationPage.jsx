import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const OtpVerificationPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { 
    loginVerify, 
    registerVerify, 
    loading, 
    error, 
    clearError 
  } = useAuth();

  // Get data from location state
  const { email, type, userData } = location.state || {};
  
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [timeLeft, setTimeLeft] = useState(300); // 5 minutes
  const [canResend, setCanResend] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  
  const inputRefs = useRef([]);

  // Redirect if no email provided
  useEffect(() => {
    if (!email || !type) {
      navigate('/login');
    }
  }, [email, type, navigate]);

  // Clear error when component mounts
  useEffect(() => {
    clearError();
  }, [clearError]);

  // Timer countdown
  useEffect(() => {
    if (timeLeft > 0) {
      const timer = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
      return () => clearTimeout(timer);
    } else {
      setCanResend(true);
    }
  }, [timeLeft]);

  // Handle OTP input change
  const handleOtpChange = (index, value) => {
    if (!/^\d*$/.test(value)) return; // Only allow digits
    
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);
    
    // Auto-focus next input
    if (value && index < 5) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  // Handle backspace
  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  // Handle paste
  const handlePaste = (e) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text').replace(/\D/g, '');
    
    if (pastedData.length === 6) {
      const newOtp = pastedData.split('');
      setOtp(newOtp);
      inputRefs.current[5]?.focus();
    }
  };

  // Format time
  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const otpCode = otp.join('');
    
    if (otpCode.length !== 6) {
      setSuccessMessage('');
      return;
    }
    
    try {
      let result;
      
      if (type === 'login') {
        result = await loginVerify(email, otpCode);
        
        if (result.success) {
          setSuccessMessage('Login successful! Redirecting...');
          setTimeout(() => {
            navigate('/dashboard');
          }, 1000);
        }
      } else if (type === 'register') {
        result = await registerVerify(email, otpCode, userData);
        
        if (result.success) {
          setSuccessMessage('Registration successful! Redirecting to login...');
          setTimeout(() => {
            navigate('/login', { 
              state: { message: 'Registration successful! Please login to continue.' }
            });
          }, 2000);
        }
      }
    } catch (err) {
      console.error('OTP verification error:', err);
    }
  };

  // Handle resend OTP
  const handleResendOtp = async () => {
    // This would need to be implemented based on your backend
    setCanResend(false);
    setTimeLeft(300);
    setSuccessMessage('New OTP sent to your email.');
  };

  // Handle back navigation
  const handleBack = () => {
    if (type === 'login') {
      navigate('/login');
    } else if (type === 'register') {
      navigate('/register');
    }
  };

  if (!email || !type) {
    return null;
  }

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="auth-card">
              <div className="auth-header text-center">
                <div className="otp-verification-icon mb-3">
                  <i className="bi bi-shield-check"></i>
                </div>
                <h2 className="auth-title">Verify Your Email</h2>
                <p className="auth-subtitle">
                  We've sent a 6-digit verification code to
                  <br />
                  <strong>{email}</strong>
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
                  {/* OTP Input Fields */}
                  <div className="mb-4">
                    <label className="form-label text-center w-100 mb-3">
                      Enter verification code
                    </label>
                    <div className="otp-inputs" onPaste={handlePaste}>
                      {otp.map((digit, index) => (
                        <input
                          key={index}
                          ref={el => inputRefs.current[index] = el}
                          type="text"
                          className="otp-input-field"
                          value={digit}
                          onChange={(e) => handleOtpChange(index, e.target.value)}
                          onKeyDown={(e) => handleKeyDown(index, e)}
                          maxLength="1"
                          autoComplete="off"
                        />
                      ))}
                    </div>
                  </div>

                  {/* Timer */}
                  <div className="text-center mb-4">
                    {timeLeft > 0 ? (
                      <div className="otp-timer">
                        <i className="bi bi-clock me-2"></i>
                        Code expires in {formatTime(timeLeft)}
                      </div>
                    ) : (
                      <div className="text-muted">
                        <i className="bi bi-exclamation-circle me-2"></i>
                        Code has expired
                      </div>
                    )}
                  </div>

                  {/* Submit Button */}
                  <button
                    type="submit"
                    className="btn btn-primary w-100 auth-submit-btn"
                    disabled={loading || otp.join('').length !== 6}
                  >
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2"></span>
                        Verifying...
                      </>
                    ) : (
                      <>
                        <i className="bi bi-check-circle me-2"></i>
                        Verify Code
                      </>
                    )}
                  </button>

                  {/* Resend Button */}
                  <div className="text-center mt-3">
                    {canResend ? (
                      <button
                        type="button"
                        className="btn btn-link p-0"
                        onClick={handleResendOtp}
                      >
                        <i className="bi bi-arrow-clockwise me-1"></i>
                        Resend Code
                      </button>
                    ) : (
                      <span className="text-muted">
                        Didn't receive the code? Check your spam folder.
                      </span>
                    )}
                  </div>

                  {/* Back Button */}
                  <button
                    type="button"
                    className="btn btn-outline-secondary w-100 mt-3"
                    onClick={handleBack}
                  >
                    <i className="bi bi-arrow-left me-2"></i>
                    Back
                  </button>
                </form>

                {/* Help Text */}
                <div className="text-center mt-4">
                  <small className="text-muted">
                    <i className="bi bi-info-circle me-1"></i>
                    If you're having trouble receiving the code, please contact support.
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

export default OtpVerificationPage;
