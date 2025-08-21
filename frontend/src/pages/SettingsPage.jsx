import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './AuthPages.css';

const SettingsPage = () => {
  const { changePassword, loading } = useAuth();
  const [settings, setSettings] = useState({
    emailNotifications: true,
    smsNotifications: false,
    marketingEmails: true,
    twoFactorAuth: false,
    darkMode: false,
    language: 'en',
    currency: 'USD',
    timezone: 'UTC'
  });
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errors, setErrors] = useState({});

  const handleSettingChange = (setting, value) => {
    setSettings(prev => ({
      ...prev,
      [setting]: value
    }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswordData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear errors
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleSaveSettings = async () => {
    try {
      // TODO: Implement API call to save settings
      console.log('Saving settings:', settings);
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccessMessage('Settings saved successfully!');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      console.error('Settings save error:', error);
      setErrors({ general: 'Failed to save settings. Please try again.' });
    }
  };

  const handlePasswordUpdate = async (e) => {
    e.preventDefault();
    
    const newErrors = {};
    
    if (!passwordData.currentPassword) {
      newErrors.currentPassword = 'Current password is required';
    }
    
    if (!passwordData.newPassword) {
      newErrors.newPassword = 'New password is required';
    } else if (passwordData.newPassword.length < 8) {
      newErrors.newPassword = 'Password must be at least 8 characters';
    }
    
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }
    
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    
    try {
      const result = await changePassword(passwordData.currentPassword, passwordData.newPassword);
      
      if (result.success) {
        setSuccessMessage('Password updated successfully!');
        setPasswordData({
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        });
        setShowPasswordForm(false);
        setTimeout(() => setSuccessMessage(''), 3000);
      } else {
        setErrors({ general: result.message });
      }
    } catch (error) {
      console.error('Password update error:', error);
      setErrors({ general: 'Failed to update password. Please try again.' });
    }
  };

  return (
    <div className="auth-container">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-10 col-lg-8">
            <div className="auth-card">
              <div className="auth-header">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <h2 className="auth-title">
                      <i className="bi bi-gear me-2"></i>
                      Settings
                    </h2>
                    <p className="auth-subtitle">
                      Manage your account preferences and security settings
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
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    {errors.general}
                  </div>
                )}

                {/* Notification Settings */}
                <div className="settings-section mb-5">
                  <h5 className="section-title">
                    <i className="bi bi-bell me-2"></i>
                    Notifications
                  </h5>
                  <div className="settings-group">
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Email Notifications</h6>
                        <p className="text-muted small">Receive booking confirmations and updates via email</p>
                      </div>
                      <div className="form-check form-switch">
                        <input
                          className="form-check-input"
                          type="checkbox"
                          checked={settings.emailNotifications}
                          onChange={(e) => handleSettingChange('emailNotifications', e.target.checked)}
                        />
                      </div>
                    </div>
                    
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>SMS Notifications</h6>
                        <p className="text-muted small">Receive important updates via SMS</p>
                      </div>
                      <div className="form-check form-switch">
                        <input
                          className="form-check-input"
                          type="checkbox"
                          checked={settings.smsNotifications}
                          onChange={(e) => handleSettingChange('smsNotifications', e.target.checked)}
                        />
                      </div>
                    </div>
                    
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Marketing Emails</h6>
                        <p className="text-muted small">Receive travel deals and promotional offers</p>
                      </div>
                      <div className="form-check form-switch">
                        <input
                          className="form-check-input"
                          type="checkbox"
                          checked={settings.marketingEmails}
                          onChange={(e) => handleSettingChange('marketingEmails', e.target.checked)}
                        />
                      </div>
                    </div>
                  </div>
                </div>

                {/* Security Settings */}
                <div className="settings-section mb-5">
                  <h5 className="section-title">
                    <i className="bi bi-shield-check me-2"></i>
                    Security
                  </h5>
                  <div className="settings-group">
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Two-Factor Authentication</h6>
                        <p className="text-muted small">Add an extra layer of security to your account</p>
                      </div>
                      <div className="form-check form-switch">
                        <input
                          className="form-check-input"
                          type="checkbox"
                          checked={settings.twoFactorAuth}
                          onChange={(e) => handleSettingChange('twoFactorAuth', e.target.checked)}
                        />
                      </div>
                    </div>
                    
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Password</h6>
                        <p className="text-muted small">Update your account password</p>
                      </div>
                      <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={() => setShowPasswordForm(!showPasswordForm)}
                      >
                        <i className="bi bi-lock me-1"></i>
                        Change Password
                      </button>
                    </div>
                  </div>

                  {/* Password Update Form */}
                  {showPasswordForm && (
                    <div className="password-form mt-3 p-3 border rounded">
                      <form onSubmit={handlePasswordUpdate}>
                        <div className="mb-3">
                          <label className="form-label">Current Password</label>
                          <input
                            type="password"
                            className={`form-control ${errors.currentPassword ? 'is-invalid' : ''}`}
                            name="currentPassword"
                            value={passwordData.currentPassword}
                            onChange={handlePasswordChange}
                            placeholder="Enter current password"
                          />
                          {errors.currentPassword && (
                            <div className="invalid-feedback">{errors.currentPassword}</div>
                          )}
                        </div>
                        
                        <div className="mb-3">
                          <label className="form-label">New Password</label>
                          <input
                            type="password"
                            className={`form-control ${errors.newPassword ? 'is-invalid' : ''}`}
                            name="newPassword"
                            value={passwordData.newPassword}
                            onChange={handlePasswordChange}
                            placeholder="Enter new password"
                          />
                          {errors.newPassword && (
                            <div className="invalid-feedback">{errors.newPassword}</div>
                          )}
                        </div>
                        
                        <div className="mb-3">
                          <label className="form-label">Confirm New Password</label>
                          <input
                            type="password"
                            className={`form-control ${errors.confirmPassword ? 'is-invalid' : ''}`}
                            name="confirmPassword"
                            value={passwordData.confirmPassword}
                            onChange={handlePasswordChange}
                            placeholder="Confirm new password"
                          />
                          {errors.confirmPassword && (
                            <div className="invalid-feedback">{errors.confirmPassword}</div>
                          )}
                        </div>
                        
                        <div className="d-flex gap-2">
                          <button type="submit" className="btn btn-primary btn-sm">
                            Update Password
                          </button>
                          <button
                            type="button"
                            className="btn btn-outline-secondary btn-sm"
                            onClick={() => {
                              setShowPasswordForm(false);
                              setPasswordData({
                                currentPassword: '',
                                newPassword: '',
                                confirmPassword: ''
                              });
                              setErrors({});
                            }}
                          >
                            Cancel
                          </button>
                        </div>
                      </form>
                    </div>
                  )}
                </div>

                {/* Preferences */}
                <div className="settings-section mb-5">
                  <h5 className="section-title">
                    <i className="bi bi-sliders me-2"></i>
                    Preferences
                  </h5>
                  <div className="settings-group">
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Language</h6>
                        <p className="text-muted small">Choose your preferred language</p>
                      </div>
                      <select
                        className="form-select"
                        value={settings.language}
                        onChange={(e) => handleSettingChange('language', e.target.value)}
                        style={{ width: 'auto' }}
                      >
                        <option value="en">English</option>
                        <option value="es">Spanish</option>
                        <option value="fr">French</option>
                        <option value="de">German</option>
                        <option value="it">Italian</option>
                      </select>
                    </div>
                    
                    <div className="setting-item">
                      <div className="setting-info">
                        <h6>Currency</h6>
                        <p className="text-muted small">Select your preferred currency</p>
                      </div>
                      <select
                        className="form-select"
                        value={settings.currency}
                        onChange={(e) => handleSettingChange('currency', e.target.value)}
                        style={{ width: 'auto' }}
                      >
                        <option value="USD">USD ($)</option>
                        <option value="EUR">EUR (€)</option>
                        <option value="GBP">GBP (£)</option>
                        <option value="JPY">JPY (¥)</option>
                        <option value="INR">INR (₹)</option>
                      </select>
                    </div>
                  </div>
                </div>

                {/* Save Button */}
                <div className="text-center">
                  <button
                    className="btn btn-primary btn-lg"
                    onClick={handleSaveSettings}
                  >
                    <i className="bi bi-check-circle me-2"></i>
                    Save All Settings
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SettingsPage;
