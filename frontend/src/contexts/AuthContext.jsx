import React, { createContext, useReducer, useEffect, useCallback } from 'react';
import { travelerAPI } from '../Routing/api';

// Auth Context
const AuthContext = createContext();

export { AuthContext };

// Auth Actions
const AUTH_ACTIONS = {
  LOGIN_START: 'LOGIN_START',
  LOGIN_SUCCESS: 'LOGIN_SUCCESS',
  LOGIN_FAILURE: 'LOGIN_FAILURE',
  LOGOUT: 'LOGOUT',
  SET_USER: 'SET_USER',
  SET_LOADING: 'SET_LOADING',
  CLEAR_ERROR: 'CLEAR_ERROR',
  SET_OTP_STAGE: 'SET_OTP_STAGE',
  REGISTRATION_START: 'REGISTRATION_START',
  REGISTRATION_SUCCESS: 'REGISTRATION_SUCCESS',
  REGISTRATION_FAILURE: 'REGISTRATION_FAILURE'
};

// Auth Reducer
const authReducer = (state, action) => {
  switch (action.type) {
    case AUTH_ACTIONS.LOGIN_START:
    case AUTH_ACTIONS.REGISTRATION_START:
      return {
        ...state,
        loading: true,
        error: null
      };
    
    case AUTH_ACTIONS.LOGIN_SUCCESS:
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload.user,
        token: action.payload.token,
        loading: false,
        error: null,
        otpStage: false
      };
    
    case AUTH_ACTIONS.LOGIN_FAILURE:
    case AUTH_ACTIONS.REGISTRATION_FAILURE:
      return {
        ...state,
        loading: false,
        error: action.payload,
        isAuthenticated: false,
        user: null,
        token: null
      };
    
    case AUTH_ACTIONS.LOGOUT:
      return {
        ...state,
        isAuthenticated: false,
        user: null,
        token: null,
        loading: false,
        error: null,
        otpStage: false
      };
    
    case AUTH_ACTIONS.SET_USER:
      return {
        ...state,
        user: action.payload,
        isAuthenticated: true
      };
    
    case AUTH_ACTIONS.SET_LOADING:
      return {
        ...state,
        loading: action.payload
      };
    
    case AUTH_ACTIONS.CLEAR_ERROR:
      return {
        ...state,
        error: null
      };
    
    case AUTH_ACTIONS.SET_OTP_STAGE:
      return {
        ...state,
        otpStage: action.payload.otpStage,
        tempEmail: action.payload.email,
        loading: false
      };
    
    case AUTH_ACTIONS.REGISTRATION_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        otpStage: false,
        tempEmail: null
      };
    
    default:
      return state;
  }
};

// Initial State
const initialState = {
  isAuthenticated: false,
  user: null,
  token: localStorage.getItem('jwt_token'),
  loading: false,
  error: null,
  otpStage: false,
  tempEmail: null
};

// AuthProvider Component
export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Fetch user profile from backend
  const fetchUserProfile = useCallback(async () => {
    try {
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
      const response = await travelerAPI.getDashboard();
      const userData = response.data;
      
      // Update localStorage with fresh data
      localStorage.setItem('user', JSON.stringify(userData));
      
      // Update state
      dispatch({
        type: AUTH_ACTIONS.SET_USER,
        payload: userData
      });
      
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
    } catch (error) {
      console.error('Error fetching user profile:', error);
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      // Don't logout for network errors - use cached user data
      const cachedUser = localStorage.getItem('user');
      const cachedToken = localStorage.getItem('jwt_token');
      
      if (cachedUser && cachedToken) {
        try {
          const userData = JSON.parse(cachedUser);
          dispatch({
            type: AUTH_ACTIONS.SET_USER,
            payload: userData
          });
          console.log('Using cached user data due to network error');
        } catch (parseError) {
          console.error('Error parsing cached user data:', parseError);
          // Only logout if cached data is corrupted
          localStorage.removeItem('jwt_token');
          localStorage.removeItem('user');
          dispatch({ type: AUTH_ACTIONS.LOGOUT });
        }
      } else {
        // No cached data available, logout
        dispatch({ type: AUTH_ACTIONS.LOGOUT });
      }
    }
  }, []);

  // Check if user is authenticated on app load and fetch fresh data
  useEffect(() => {
    const token = localStorage.getItem('jwt_token');
    
    if (token) {
      // Token exists, fetch fresh user data from backend
      fetchUserProfile();
    }
  }, [fetchUserProfile]);

  // Login function (Step 1: Send credentials, receive OTP)
  const loginInit = async (credentials) => {
    dispatch({ type: AUTH_ACTIONS.LOGIN_START });
    
    try {
      await travelerAPI.initiateLogin(credentials);
      
      dispatch({
        type: AUTH_ACTIONS.SET_OTP_STAGE,
        payload: {
          otpStage: true,
          email: credentials.email
        }
      });
      
      return {
        success: true,
        message: 'OTP sent to your email. Please verify to complete login.',
        otpRequired: true
      };
    } catch (error) {
      const errorMessage = error.message || 'Login failed. Please try again.';
      dispatch({
        type: AUTH_ACTIONS.LOGIN_FAILURE,
        payload: errorMessage
      });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Login verification (Step 2: Verify OTP and get JWT)
  const loginVerify = async (email, otp) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      const response = await travelerAPI.verifyLogin({
        email,
        otp
      });
      
      const token = response.data;
      
      // Store token
      localStorage.setItem('jwt_token', token);
      
      // Get user profile
      const userResponse = await travelerAPI.getDashboard();
      const userData = userResponse.data;
      
      // Store user data
      localStorage.setItem('user', JSON.stringify(userData));
      
      dispatch({
        type: AUTH_ACTIONS.LOGIN_SUCCESS,
        payload: {
          user: userData,
          token
        }
      });
      
      return {
        success: true,
        message: 'Login successful!',
        user: userData
      };
    } catch (error) {
      const errorMessage = error.message || 'OTP verification failed. Please try again.';
      dispatch({
        type: AUTH_ACTIONS.LOGIN_FAILURE,
        payload: errorMessage
      });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Registration (Step 1: Send details, receive OTP)
  const registerInit = async (userData) => {
    dispatch({ type: AUTH_ACTIONS.REGISTRATION_START });
    
    try {
      await travelerAPI.initiateRegistration(userData);
      
      dispatch({
        type: AUTH_ACTIONS.SET_OTP_STAGE,
        payload: {
          otpStage: true,
          email: userData.email
        }
      });
      
      return {
        success: true,
        message: 'OTP sent to your email for registration verification.',
        otpRequired: true
      };
    } catch (error) {
      const errorMessage = error.message || 'Registration failed. Please try again.';
      dispatch({
        type: AUTH_ACTIONS.REGISTRATION_FAILURE,
        payload: errorMessage
      });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Registration verification (Step 2: Verify OTP and complete registration)
  const registerVerify = async (email, otp, userData) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      const verificationData = {
        ...userData,
        email,
        otp
      };
      
      await travelerAPI.verifyRegistration(verificationData);
      
      dispatch({ type: AUTH_ACTIONS.REGISTRATION_SUCCESS });
      
      return {
        success: true,
        message: 'Registration successful! Please login to continue.'
      };
    } catch (error) {
      const errorMessage = error.message || 'Registration verification failed. Please try again.';
      dispatch({
        type: AUTH_ACTIONS.REGISTRATION_FAILURE,
        payload: errorMessage
      });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Forgot Password
  const forgotPassword = async (email) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      await travelerAPI.forgotPassword(email);
      
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: true,
        message: 'Password reset OTP sent to your email.'
      };
    } catch (error) {
      const errorMessage = error.message || 'Failed to send password reset OTP.';
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Reset Password with OTP
  const resetPassword = async (email, newPassword, otp) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      await travelerAPI.resetPassword({
        email,
        newPassword,
        otp
      });
      
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: true,
        message: 'Password reset successful! Please login with your new password.'
      };
    } catch (error) {
      const errorMessage = error.message || 'Password reset failed. Please try again.';
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Update Profile
  const updateProfile = async (profileData) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      console.log('AuthContext: Calling travelerAPI.updateProfile with:', profileData);
      const response = await travelerAPI.updateProfile(profileData);
      const updatedUser = response.data;
      
      console.log('AuthContext: Received updated user:', updatedUser);
      
      // Update local storage
      localStorage.setItem('user', JSON.stringify(updatedUser));
      
      // Update state
      dispatch({
        type: AUTH_ACTIONS.SET_USER,
        payload: updatedUser
      });
      
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: true,
        message: 'Profile updated successfully!',
        user: updatedUser
      };
    } catch (error) {
      console.error('AuthContext: Profile update error:', error);
      const errorMessage = error.message || 'Failed to update profile.';
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Change Password
  const changePassword = async (oldPassword, newPassword) => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      await travelerAPI.changePassword({
        oldPassword,
        newPassword
      });
      
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: true,
        message: 'Password changed successfully!'
      };
    } catch (error) {
      const errorMessage = error.message || 'Failed to change password.';
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Delete Profile
  const deleteProfile = async () => {
    dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
    
    try {
      await travelerAPI.deleteProfile();
      
      // Clear local storage and state
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('user');
      
      dispatch({ type: AUTH_ACTIONS.LOGOUT });
      
      return {
        success: true,
        message: 'Account deleted successfully!'
      };
    } catch (error) {
      const errorMessage = error.message || 'Failed to delete account.';
      dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      
      return {
        success: false,
        message: errorMessage
      };
    }
  };

  // Logout
  const logout = async () => {
    try {
      await travelerAPI.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear local storage and state
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('user');
      
      dispatch({ type: AUTH_ACTIONS.LOGOUT });
    }
  };

  // Clear error
  const clearError = useCallback(() => {
    dispatch({ type: AUTH_ACTIONS.CLEAR_ERROR });
  }, []);

  // Context value
  const value = {
    ...state,
    loginInit,
    loginVerify,
    registerInit,
    registerVerify,
    forgotPassword,
    resetPassword,
    updateProfile,
    changePassword,
    deleteProfile,
    logout,
    clearError,
    fetchUserProfile
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
