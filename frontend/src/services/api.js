import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => apiClient.post('/auth/traveler/login', credentials),
  register: (userData) => apiClient.post('/auth/traveler/register/init', userData),
  verifyRegistration: (userData) => apiClient.post('/auth/traveler/register/verify', userData),
  logout: () => apiClient.post('/auth/traveler/logout'),
  getProfile: () => apiClient.get('/traveler/profile'),
  updateProfile: (userData) => apiClient.put('/traveler/profile/update', userData),
  forgotPassword: (email) => apiClient.post('/auth/traveler/forgot-password', { email }),
  resetPassword: (data) => apiClient.post('/auth/traveler/reset-password', data),
};

// Tours API
export const toursAPI = {
  getAllTours: () => apiClient.get('/public/tours'),
  getTourById: (id) => apiClient.get(`/public/tours/${id}`),
  searchTours: (params) => apiClient.get('/public/tours/search', { params }),
  getFeaturedTours: () => apiClient.get('/public/tours/featured'),
};

// Destinations API
export const destinationsAPI = {
  getAllDestinations: () => apiClient.get('/public/destinations'),
  getDestinationById: (id) => apiClient.get(`/public/destinations/${id}`),
  getPopularDestinations: () => apiClient.get('/public/destinations/popular'),
};

// Bookings API
export const bookingsAPI = {
  createBooking: (bookingData) => apiClient.post('/traveler/bookings', bookingData),
  getMyBookings: () => apiClient.get('/traveler/bookings'),
  getBookingById: (id) => apiClient.get(`/traveler/bookings/${id}`),
  cancelBooking: (id) => apiClient.put(`/traveler/bookings/${id}/cancel`),
};

// Payment API
export const paymentAPI = {
  processPayment: (paymentData) => apiClient.post('/payment/process', paymentData),
  getPaymentHistory: () => apiClient.get('/traveler/payments'),
};

export default apiClient;
