import api from '../Routing/api';

// Service for handling tour-related API calls
export const tourService = {
  // Get all featured tours
  getFeaturedTours: async () => {
    try {
      const response = await api.get('/tours/featured');
      return response.data;
    } catch (error) {
      console.error('Error fetching featured tours:', error);
      // Return mock data if API is not available
      return [
        {
          id: 1,
          image: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2000&q=80",
          title: "Santorini Paradise",
          description: "Experience the breathtaking beauty of Santorini with its iconic blue domes and stunning sunsets.",
          price: "$1,299",
          duration: "7 Days"
        },
        {
          id: 2,
          image: "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80",
          title: "Swiss Alps Adventure",
          description: "Discover the majestic Swiss Alps with guided hiking tours and mountain adventures.",
          price: "$1,899",
          duration: "10 Days"
        },
        {
          id: 3,
          image: "https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2000&q=80",
          title: "Tropical Bali Retreat",
          description: "Relax in paradise with pristine beaches, cultural experiences, and luxury accommodations.",
          price: "$999",
          duration: "5 Days"
        }
      ];
    }
  },

  // Search for tours
  searchTours: async (searchParams) => {
    try {
      const response = await api.get('/tours/search', { params: searchParams });
      return response.data;
    } catch (error) {
      console.error('Error searching tours:', error);
      throw error;
    }
  },

  // Get tour details by ID
  getTourDetails: async (tourId) => {
    try {
      const response = await api.get(`/tours/${tourId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching tour details:', error);
      throw error;
    }
  },

  // Get testimonials
  getTestimonials: async () => {
    try {
      const response = await api.get('/testimonials');
      return response.data;
    } catch (error) {
      console.error('Error fetching testimonials:', error);
      // Return mock data if API is not available
      return [
        {
          id: 1,
          name: "Sarah Johnson",
          quote: "TourVerse made our honeymoon unforgettable! The attention to detail and personalized service exceeded all expectations.",
          image: "https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
          rating: 5
        },
        {
          id: 2,
          name: "Mike Chen",
          quote: "Amazing adventure tours! The guides were knowledgeable and the experiences were once-in-a-lifetime.",
          image: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
          rating: 5
        },
        {
          id: 3,
          name: "Emily Rodriguez",
          quote: "Perfectly organized trip with seamless logistics. I'll definitely book with TourVerse again!",
          image: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=150&q=80",
          rating: 5
        }
      ];
    }
  }
};

export default tourService;
