import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const ProfilePage = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, updateProfile, deleteProfile, fetchUserProfile } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    profilePicture: null
  });
  const [profilePreview, setProfilePreview] = useState(null);
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);

  // Redirect if not authenticated
  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Initialize form data with user data (only when user changes)
  useEffect(() => {
    if (user) {
      setFormData(prevFormData => {
        // Only update if data actually changed
        if (prevFormData.name !== (user.name || '') || prevFormData.phone !== (user.phone || '')) {
          return {
            name: user.name || '',
            phone: user.phone || '',
            profilePicture: null
          };
        }
        return prevFormData;
      });
      // Only set profile preview if it exists and is a valid URL
      if (user.profilePictureUrl && !user.profilePictureUrl.includes('placeholder')) {
        setProfilePreview(user.profilePictureUrl);
      } else {
        setProfilePreview(null);
      }
    }
  }, [user]);

  const handleInputChange = (e) => {
    const { name, value, files } = e.target;
    
    console.log('Input changed:', name, value); // Debug log
    
    if (name === 'profilePicture' && files[0]) {
      const file = files[0];
      
      // Validate file size (5MB max)
      if (file.size > 5 * 1024 * 1024) {
        setErrors(prev => ({ ...prev, profilePicture: 'File size must be less than 5MB' }));
        return;
      }
      
      // Validate file type
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
      if (!allowedTypes.includes(file.type)) {
        setErrors(prev => ({ ...prev, profilePicture: 'Only JPEG, PNG and GIF images are allowed' }));
        return;
      }
      
      // Clear any previous errors
      setErrors(prev => ({ ...prev, profilePicture: '' }));
      
      // Set file and preview
      setFormData(prev => ({ ...prev, profilePicture: file }));
      setProfilePreview(URL.createObjectURL(file));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
      // Clear errors when user starts typing
      if (errors[name]) {
        setErrors(prev => ({ ...prev, [name]: '' }));
      }
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    // Name validation
    if (!formData.name || formData.name.trim().length < 2) {
      newErrors.name = 'Name must be at least 2 characters long';
    } else if (formData.name.trim().length > 100) {
      newErrors.name = 'Name must be less than 100 characters';
    } else if (!/^[a-zA-Z\s]+$/.test(formData.name.trim())) {
      newErrors.name = 'Name can only contain letters and spaces';
    }
    
    // Phone validation
    if (!formData.phone || !/^\d{10}$/.test(formData.phone.replace(/\D/g, ''))) {
      newErrors.phone = 'Please enter a valid 10-digit phone number';
    }
    
    // Profile picture validation
    if (formData.profilePicture) {
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
      if (!allowedTypes.includes(formData.profilePicture.type)) {
        newErrors.profilePicture = 'Only JPEG, PNG and GIF images are allowed';
      }
      
      const maxSize = 5 * 1024 * 1024; // 5MB
      if (formData.profilePicture.size > maxSize) {
        newErrors.profilePicture = 'Image size must be less than 5MB';
      }
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSaveProfile = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    setIsSubmitting(true);
    setErrors({});
    setUploadProgress(0);
    
    try {
      // Show upload progress for better UX
      setUploadProgress(25);
      
      // Create FormData for multipart request (matches backend @ModelAttribute)
      const updateFormData = new FormData();
      let hasChanges = false;
      
      // Always include name and phone to ensure complete update
      updateFormData.append('name', formData.name.trim());
      updateFormData.append('phone', formData.phone.trim());
      hasChanges = true;
      
      setUploadProgress(50);
      
      // Include profile picture if selected
      if (formData.profilePicture) {
        updateFormData.append('profilePicture', formData.profilePicture);
        setUploadProgress(75);
      }
      
      console.log('Sending FormData to backend with fields:');
      for (let [key, value] of updateFormData.entries()) {
        console.log(`${key}:`, value);
      }
      
      if (!hasChanges) {
        setSuccessMessage('No changes to save.');
        setIsEditing(false);
        setIsSubmitting(false);
        setUploadProgress(0);
        return;
      }
      
      setUploadProgress(90);
      const result = await updateProfile(updateFormData);
      setUploadProgress(100);
      
      if (result.success) {
        // Always fetch the latest user profile from backend after update
        await fetchUserProfile();
        setSuccessMessage('Profile updated successfully! 🎉');
        setIsEditing(false);
        // Reset form data with updated user data
        if (user) {
          setFormData({
            name: user.name || '',
            phone: user.phone || '',
            profilePicture: null
          });
          setProfilePreview(user.profilePictureUrl);
        }
        // Clear success message after 5 seconds
        setTimeout(() => {
          setSuccessMessage('');
        }, 5000);
      } else {
        setErrors({ general: result.message });
      }
    } catch (error) {
      console.error('Profile update error:', error);
      setErrors({ general: 'Failed to update profile. Please try again.' });
    } finally {
      setIsSubmitting(false);
      setUploadProgress(0);
    }
  };

  const handleDeleteAccount = async () => {
    try {
      const result = await deleteProfile();
      
      if (result.success) {
        // Redirect to login after successful deletion
        navigate('/login', { replace: true });
      } else {
        setErrors({ general: result.message });
        setShowDeleteModal(false);
      }
    } catch (error) {
      console.error('Account deletion error:', error);
      setErrors({ general: 'Failed to delete account. Please try again.' });
      setShowDeleteModal(false);
    }
  };

  // Handle edit button click
  const handleEditClick = () => {
    console.log('Edit button clicked, current isEditing:', isEditing);
    setIsEditing(true);
    setErrors({}); // Clear any previous errors
    console.log('After setIsEditing(true)');
    
    // Force focus on first editable field after state update
    setTimeout(() => {
      const nameInput = document.querySelector('input[name="name"]');
      if (nameInput) {
        nameInput.focus();
      }
    }, 100);
  };

  // Handle cancel button click
  const handleCancelClick = () => {
    setIsEditing(false);
    setErrors({});
    // Reset form data to original user data
    if (user) {
      setFormData({
        name: user.name || '',
        phone: user.phone || '',
        profilePicture: null
      });
      // Only set preview if user has a valid profile picture URL
      if (user.profilePictureUrl && !user.profilePictureUrl.includes('placeholder')) {
        setProfilePreview(user.profilePictureUrl);
      } else {
        setProfilePreview(null);
      }
    }
  };

  const clearMessages = () => {
    setErrors({});
    setSuccessMessage('');
  };

  const removeProfilePicture = () => {
    setFormData(prev => ({ ...prev, profilePicture: null }));
    // Only set preview if user has a valid profile picture URL
    if (user?.profilePictureUrl && !user.profilePictureUrl.includes('placeholder')) {
      setProfilePreview(user.profilePictureUrl);
    } else {
      setProfilePreview(null);
    }
  };

  if (!user) {
    return (
      <div className="auth-container">
        <div className="container py-5">
          <div className="row justify-content-center">
            <div className="col-md-6 text-center">
              <div className="spinner-border text-primary mb-3" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <h5>Loading profile...</h5>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-8 col-lg-6">
            <div className="auth-card">
              <div className="auth-header">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <h2 className="auth-title">
                      <i className="bi bi-person-circle me-2"></i>
                      My Profile
                    </h2>
                    <p className="auth-subtitle">
                      Manage your personal information and account settings
                    </p>
                  </div>
                  <Link to="/dashboard" className="btn btn-outline-secondary">
                    <i className="bi bi-arrow-left me-1"></i>
                    Back to Dashboard
                  </Link>
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
                {errors.general && (
                  <div className="alert alert-danger" role="alert">
                    <i className="bi bi-exclamation-circle me-2"></i>
                    {errors.general}
                    <button type="button" className="btn-close" onClick={clearMessages}></button>
                  </div>
                )}

                {/* Profile Picture Section */}
                <div className="text-center mb-4">
                  <div className="profile-picture-container">
                    {profilePreview ? (
                      <img
                        src={profilePreview}
                        alt="Profile"
                        className="profile-preview"
                        width="120"
                        height="120"
                        onError={(e) => {
                          console.error('Failed to load profile image:', e.target.src);
                          setProfilePreview(null);
                        }}
                      />
                    ) : (
                      <div className="profile-placeholder">
                        {user.name ? user.name.charAt(0).toUpperCase() : 'U'}
                      </div>
                    )}
                  </div>
                  
                  {isEditing && (
                    <div className="profile-actions">
                      <div>
                        <label className="btn btn-sm btn-outline-primary me-2">
                          <i className="bi bi-camera me-1"></i>
                          Change Photo
                          <input
                            type="file"
                            name="profilePicture"
                            accept="image/*"
                            onChange={handleInputChange}
                            className="d-none"
                          />
                        </label>
                        {profilePreview && (
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-danger"
                            onClick={removeProfilePicture}
                          >
                            <i className="bi bi-trash me-1"></i>
                            Remove
                          </button>
                        )}
                      </div>
                    </div>
                  )}
                  {errors.profilePicture && (
                    <div className="text-danger small mt-2">{errors.profilePicture}</div>
                  )}
                </div>

                {/* Profile Form */}
                <form onSubmit={handleSaveProfile}>
                  {/* Debug - Current State */}
                  <div className="mb-2">
                    <small className="text-muted">
                      Status: {isEditing ? '✅ Editing Mode' : '🔒 Read-Only Mode'}
                    </small>
                  </div>
                  
                  {/* Name Field */}
                  <div className="mb-3">
                    <label className="form-label">
                      <i className="bi bi-person me-2"></i>
                      Full Name
                    </label>
                    <input
                      type="text"
                      className={`form-control ${errors.name ? 'is-invalid' : ''} ${isEditing ? 'border-primary' : ''}`}
                      name="name"
                      value={formData.name}
                      onChange={handleInputChange}
                      readOnly={!isEditing}
                      placeholder="Enter your full name"
                      style={{ 
                        backgroundColor: isEditing ? '#fff' : '#f8f9fa',
                        cursor: isEditing ? 'text' : 'default'
                      }}
                    />
                    {errors.name && (
                      <div className="invalid-feedback">{errors.name}</div>
                    )}
                  </div>

                  {/* Email Field (Read-only) */}
                  <div className="mb-3">
                    <label className="form-label">
                      <i className="bi bi-envelope me-2"></i>
                      Email Address
                    </label>
                    <input
                      type="email"
                      className="form-control"
                      value={user.email || ''}
                      disabled
                      placeholder="Email address"
                    />
                    <div className="form-text">
                      <i className="bi bi-info-circle me-1"></i>
                      Email cannot be changed. Contact support if you need to update your email.
                    </div>
                  </div>

                  {/* Phone Field */}
                  <div className="mb-4">
                    <label className="form-label">
                      <i className="bi bi-telephone me-2"></i>
                      Phone Number
                    </label>
                    <input
                      type="tel"
                      className={`form-control ${errors.phone ? 'is-invalid' : ''} ${isEditing ? 'border-primary' : ''}`}
                      name="phone"
                      value={formData.phone}
                      onChange={handleInputChange}
                      readOnly={!isEditing}
                      placeholder="Enter your phone number"
                      style={{ 
                        backgroundColor: isEditing ? '#fff' : '#f8f9fa',
                        cursor: isEditing ? 'text' : 'default'
                      }}
                    />
                    {errors.phone && (
                      <div className="invalid-feedback">{errors.phone}</div>
                    )}
                  </div>

                  {/* Action Buttons */}
                  <div className="d-flex flex-column flex-md-row gap-2">
                    {!isEditing ? (
                      <>
                        <button
                          type="button"
                          className="btn btn-primary flex-fill"
                          onClick={handleEditClick}
                        >
                          <i className="bi bi-pencil me-2"></i>
                          Update Profile
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-danger flex-fill"
                          onClick={() => setShowDeleteModal(true)}
                        >
                          <i className="bi bi-trash me-2"></i>
                          Delete Account
                        </button>
                      </>
                    ) : (
                      <>
                        {/* Upload Progress Bar */}
                        {isSubmitting && uploadProgress > 0 && (
                          <div className="mb-3">
                            <div className="d-flex justify-content-between align-items-center mb-1">
                              <small className="text-muted">Updating Profile...</small>
                              <small className="text-muted">{uploadProgress}%</small>
                            </div>
                            <div className="progress" style={{ height: '4px' }}>
                              <div 
                                className="progress-bar progress-bar-striped progress-bar-animated" 
                                style={{ width: `${uploadProgress}%` }}
                              ></div>
                            </div>
                          </div>
                        )}
                        
                        <button
                          type="submit"
                          className="btn btn-success flex-fill"
                          disabled={isSubmitting}
                        >
                          {isSubmitting ? (
                            <>
                              <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                              Saving...
                            </>
                          ) : (
                            <>
                              <i className="bi bi-check me-2"></i>
                              Save Changes
                            </>
                          )}
                        </button>
                        <button
                          type="button"
                          className="btn btn-outline-secondary flex-fill"
                          onClick={handleCancelClick}
                          disabled={isSubmitting}
                        >
                          <i className="bi bi-x me-2"></i>
                          Cancel
                        </button>
                      </>
                    )}
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Delete Account Modal */}
      {showDeleteModal && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header border-0">
                <h5 className="modal-title text-danger">
                  <i className="bi bi-exclamation-triangle me-2"></i>
                  Delete Account
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowDeleteModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <p className="mb-3">
                  Are you sure you want to delete your account? This action cannot be undone.
                </p>
                <div className="alert alert-warning">
                  <i className="bi bi-warning me-2"></i>
                  <strong>Warning:</strong> All your data will be permanently removed.
                </div>
              </div>
              <div className="modal-footer border-0">
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => setShowDeleteModal(false)}
                >
                  Cancel
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={handleDeleteAccount}
                >
                  <i className="bi bi-trash me-2"></i>
                  Delete Account
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
