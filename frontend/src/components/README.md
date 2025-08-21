# TourVerse HomePage Component

A modern, responsive homepage component built with React and Bootstrap, inspired by the Mixly travel agency template.

## Features

### 🎯 Key Components

1. **Navigation Bar**
   - Fixed navigation with scroll effects
   - Responsive mobile menu
   - Smooth animations and hover effects
   - Brand logo with gradient text

2. **Hero Section**
   - Full-screen background with overlay
   - Compelling headline and call-to-action
   - Parallax background effect
   - Responsive typography

3. **Search Bar**
   - Advanced search form with destination, activity, and date filters
   - Clean, modern input styling
   - Form validation ready
   - Responsive grid layout

4. **Featured Tours**
   - Card-based layout with hover effects
   - High-quality images from Unsplash
   - Price and duration display
   - Smooth animations on scroll

5. **Why Choose Us Section**
   - Icon-based feature highlights
   - 4-column responsive grid
   - Gradient background with overlay
   - Interactive hover animations

6. **Testimonials**
   - Customer reviews with ratings
   - Profile images and customer names
   - Card-based responsive layout
   - Star rating display

7. **Footer**
   - Comprehensive link organization
   - Social media icons
   - Responsive column layout
   - Scroll-to-top functionality

### 🎨 Design Features

- **Modern UI/UX**: Clean, contemporary design with consistent spacing
- **Responsive Design**: Fully responsive across all device sizes
- **Smooth Animations**: CSS animations and scroll-triggered effects
- **Color Scheme**: Professional blue and orange theme for travel industry
- **Typography**: Modern font stack with proper hierarchy
- **Accessibility**: Focus states, proper contrast, and semantic HTML

### 🛠️ Technical Implementation

- **React Hooks**: useState and useEffect for state management
- **Intersection Observer**: Scroll-triggered animations
- **CSS Custom Properties**: Consistent theming with CSS variables
- **Bootstrap Grid**: Responsive layout system
- **Bootstrap Icons**: Scalable vector icons
- **Flexbox/Grid**: Modern CSS layout techniques

### 📱 Responsive Breakpoints

- **Desktop**: 1200px and above
- **Tablet**: 768px - 1199px
- **Mobile**: 576px - 767px
- **Small Mobile**: Below 576px

### 🚀 Performance Optimizations

- **Lazy Loading**: Images load as needed
- **CSS Optimization**: Efficient selectors and minimal repaints
- **Smooth Scrolling**: Hardware-accelerated animations
- **Accessibility**: Reduced motion support for users with preferences

### 🔗 Integration Points

- **API Service**: tourService.js for backend integration
- **Search Functionality**: Ready for tour search implementation
- **Navigation**: Prepared for React Router integration
- **Form Handling**: Search form ready for API calls

### 📦 Dependencies

- React 19.1.1
- Bootstrap 5.3.7
- Bootstrap Icons 1.13.1
- Axios (for API calls)

### 🎯 Usage

```jsx
import HomePage from './components/HomePage';

function App() {
  return <HomePage />;
}
```

### 🔧 Customization

The component uses CSS custom properties for easy theming:

```css
:root {
  --primary-color: #0066cc;
  --secondary-color: #ff6b35;
  --accent-color: #ffd700;
  /* ... more variables */
}
```

### 📸 Image Sources

All images are sourced from Unsplash with proper licensing:

- Hero background: Travel destination imagery
- Tour cards: Destination-specific photos
- Testimonials: Professional portrait photography

### 🎨 Color Palette

- **Primary Blue**: #0066cc (Trust, reliability)
- **Secondary Orange**: #ff6b35 (Adventure, excitement)
- **Accent Gold**: #ffd700 (Premium, luxury)
- **Text Dark**: #2c3e50 (Readability)
- **Text Light**: #6c757d (Secondary content)

This homepage component provides a solid foundation for a travel agency website with modern design principles and ready-to-implement functionality.
