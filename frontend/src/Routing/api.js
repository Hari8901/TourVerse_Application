import axios from 'axios';

// Check if backend is available
// Use Vite dev proxy during development to avoid CORS
const API_URL = '/api';
const MOCK_MODE = false; // Set to false when backend is available

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 15000, // Increased to 15 seconds
    withCredentials: true, // Enable credentials for CORS
});

// Request interceptor to add auth token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwt_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor to handle errors globally
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        // Handle different types of errors
        if (error.code === 'ECONNABORTED' || error.message.includes('timeout')) {
            // Timeout error
            console.warn('Backend timeout - using mock mode');
            return Promise.reject({
                message: 'Backend connection timeout. Using offline mode.',
                status: 0
            });
        }
        
        if (error.response) {
            // Server responded with error status
            const { status, data } = error.response;
            
            if (status === 401) {
                // Unauthorized - redirect to login
                localStorage.removeItem('jwt_token');
                localStorage.removeItem('user');
                window.location.href = '/login';
                return Promise.reject({
                    message: 'Session expired. Please login again.',
                    status
                });
            }
            
            if (status === 403) {
                return Promise.reject({
                    message: data.message || 'Access forbidden',
                    status
                });
            }
            
            if (status === 404) {
                return Promise.reject({
                    message: data.message || 'Resource not found',
                    status
                });
            }
            
            if (status === 409) {
                return Promise.reject({
                    message: data.message || 'Conflict occurred',
                    status
                });
            }
            
            if (status >= 500) {
                return Promise.reject({
                    message: data.message || 'Server error occurred. Please try again later.',
                    status
                });
            }
            
            // Generic error with backend message
            return Promise.reject({
                message: data.message || 'An error occurred',
                status,
                details: data.details
            });
        } else if (error.request) {
            // Network error - backend not available
            console.warn('Backend not available - using mock mode');
            return Promise.reject({
                message: 'Backend not available. Using offline mode.',
                status: 0
            });
        } else {
            // Other error
            return Promise.reject({
                message: error.message || 'An unexpected error occurred',
                status: 0
            });
        }
    }
);

// Traveler Authentication API endpoints
export const travelerAPI = {
    // Authentication
    initiateRegistration: (data) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'OTP sent to your email for registration verification.' });
        }
        const formData = new FormData();
        Object.keys(data).forEach(key => {
            if (data[key] !== null && data[key] !== undefined) {
                formData.append(key, data[key]);
            }
        });
        return api.post('/traveler/register/init', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    verifyRegistration: (data) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'Registration successful!' });
        }
        const formData = new FormData();
        Object.keys(data).forEach(key => {
            if (data[key] !== null && data[key] !== undefined) {
                formData.append(key, data[key]);
            }
        });
        return api.post('/traveler/register/verify', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    initiateLogin: (data) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'OTP sent to your email.' });
        }
        return api.post('/traveler/login/init', data);
    },
    
    verifyLogin: (data) => {
        if (MOCK_MODE) {
            // Mock JWT token
            return Promise.resolve({ data: 'mock-jwt-token-12345' });
        }
        return api.post('/traveler/login/verify', data);
    },
    
    logout: () => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'Logged out successfully' });
        }
        return api.post('/traveler/logout');
    },
    
    forgotPassword: (email) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'Password reset OTP sent.' });
        }
        return api.post('/traveler/forgot-password', { email });
    },
    
    resetPassword: (data) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'Password reset successful.' });
        }
        return api.post('/traveler/reset-password-otp', data);
    },
    
    // Profile Management
    getDashboard: () => {
        if (MOCK_MODE) {
            const mockUser = {
                id: 1,
                name: 'Hareshwar',
                email: 'avhadhari7@gmail.com',
                phone: '7666736126',
                profilePictureUrl: null // Set to null to avoid placeholder 404 errors
            };
            return Promise.resolve({ data: mockUser });
        }
        return api.get('/traveler/dashboard');
    },
    
    updateProfile: (data) => {
        if (MOCK_MODE) {
            // Simulate successful profile update
            const currentUser = JSON.parse(localStorage.getItem('user') || '{}');
            
            // Handle FormData in mock mode
            let name = currentUser.name;
            let phone = currentUser.phone;
            let profilePictureUrl = currentUser.profilePictureUrl;
            
            if (data instanceof FormData) {
                if (data.has('name')) name = data.get('name');
                if (data.has('phone')) phone = data.get('phone');
                if (data.has('profilePicture')) {
                    profilePictureUrl = URL.createObjectURL(data.get('profilePicture'));
                }
            } else {
                name = data.name || name;
                phone = data.phone || phone;
                if (data.profilePicture) {
                    profilePictureUrl = URL.createObjectURL(data.profilePicture);
                }
            }
            
            const updatedUser = {
                ...currentUser,
                name,
                phone,
                profilePictureUrl
            };
            
            // Update localStorage
            localStorage.setItem('user', JSON.stringify(updatedUser));
            
            return Promise.resolve({ data: updatedUser });
        }
        
        // Real API call - backend expects FormData for multipart/form-data
        let formData;
        if (data instanceof FormData) {
            formData = data;
        } else {
            // Convert object to FormData
            formData = new FormData();
            Object.keys(data).forEach(key => {
                if (data[key] !== null && data[key] !== undefined) {
                    formData.append(key, data[key]);
                }
            });
        }
        
        return api.put('/traveler/profile/update', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    changePassword: (data) => {
        if (MOCK_MODE) {
            return Promise.resolve({ data: 'Password changed successfully.' });
        }
        return api.post('/traveler/profile/change-password', {
            oldPassword: data.oldPassword || data.currentPassword,
            newPassword: data.newPassword
        });
    },
    
    deleteProfile: () => {
        if (MOCK_MODE) {
            // Clear localStorage
            localStorage.removeItem('user');
            localStorage.removeItem('jwt_token');
            return Promise.resolve({ data: 'Account deleted successfully.' });
        }
        return api.delete('/traveler/profile');
    },
};

export default api;