# 🌍 TourVerse Application

A comprehensive **Travel Guide Booking System** that connects travelers with professional tour guides, enabling seamless booking experiences and real-time communication.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

TourVerse is a modern travel guide booking platform that facilitates connections between tourists and professional tour guides. The application provides a seamless experience for:

- **Tourists**: Browse, search, and book tour guides with real-time availability
- **Tour Guides**: Manage profiles, availability, and bookings
- **Administrators**: Oversee platform operations and user management

## ✨ Features

### Core Features
- 🔐 **Authentication & Authorization** - JWT-based secure authentication
- 👤 **User Management** - Role-based access (Tourist, Guide, Admin)
- 🔍 **Tour Search & Discovery** - Advanced search with filters
- 📅 **Booking System** - Real-time availability and booking management
- 💬 **Real-time Chat** - WebSocket-based communication between users
- 💳 **Payment Integration** - Secure payment processing with Stripe
- ⭐ **Review System** - Rating and feedback system
- 📱 **Responsive Design** - Mobile-friendly user interface

### Advanced Features
- 🌐 **Multi-database Architecture** - MySQL for relational data, MongoDB for documents
- 📊 **Analytics Dashboard** - Booking insights and performance metrics
- 🌍 **Location Services** - Geolocation-based guide discovery
- ☁️ **Cloud Storage** - AWS S3 integration for media files
- 🔄 **Real-time Updates** - Live booking status and notifications

## 🛠 Tech Stack

### Frontend
- **React 19.1** - Modern UI library with hooks and concurrent features
- **Vite 7.0** - Fast build tool and development server
- **React Router DOM 7.7** - Client-side routing and navigation
- **Bootstrap 5.3** - CSS framework for responsive design
- **Bootstrap Icons** - Icon library for UI components
- **Axios 1.11** - HTTP client for API calls
- **Context API** - State management for authentication
- **WebSocket (SockJS + STOMP)** - Real-time communication
- **Formik 2.4** - Form management and validation

### Backend
- **Spring Boot 3.x** - Enterprise Java framework
- **Spring Security 6** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring WebSocket** - Real-time communication support
- **JWT** - Token-based authentication
- **Maven 3.9** - Dependency management and build tool
- **Java 21** - Latest LTS version with modern features

### Databases
- **MySQL 8.0+** - Primary relational database for structured data
- **MongoDB 6.0+** - Document database for chat messages and reviews
- **H2 Database** - In-memory database for development and testing

### Cloud & DevOps
- **AWS S3** - Object storage for media files
- **Docker** - Containerization
- **Kubernetes** - Container orchestration
- **Terraform** - Infrastructure as Code
- **Jenkins** - CI/CD pipeline
- **GitHub Actions** - Automated workflows

## 🏗 Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   Databases     │
│   (React)       │◄──►│  (Spring Boot)  │◄──►│   MySQL +       │
│                 │    │                 │    │   MongoDB       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                        ┌─────────────────┐
                        │  External APIs  │
                        │  (Stripe, AWS)  │
                        └─────────────────┘
```

## 📁 Project Structure

```
TourVerse_Application/
├── backend/                    # Spring Boot backend application
│   ├── src/main/java/com/tourverse/
│   │   ├── backend/           # Main application package
│   │   │   ├── auth/          # Authentication & authorization
│   │   │   ├── booking/       # Booking management
│   │   │   ├── chat/          # Real-time chat functionality
│   │   │   ├── common/        # Shared utilities and config
│   │   │   ├── guide/         # Tour guide management
│   │   │   ├── payment/       # Payment processing
│   │   │   ├── review/        # Review and rating system
│   │   │   ├── tour/          # Tour management
│   │   │   └── user/          # User management
│   ├── src/main/resources/    # Configuration files
│   │   ├── application.properties         # Main config
│   │   ├── application-dev.properties     # Development config
│   │   ├── application-prod.properties    # Production config
│   │   └── application-test.properties    # Test config
│   ├── src/test/java/         # Unit and integration tests
│   ├── target/                # Build output directory
│   └── pom.xml                # Maven dependencies
│
├── frontend/                   # React frontend application
│   ├── src/
│   │   ├── api/               # API service layer
│   │   ├── assets/            # Static assets and styles
│   │   │   └── styles/        # CSS files
│   │   ├── components/        # Reusable UI components
│   │   │   ├── auth/          # Authentication components
│   │   │   ├── common/        # Common UI components
│   │   │   └── layout/        # Layout components (Navbar, Footer)
│   │   ├── contexts/          # React context providers
│   │   ├── features/          # Feature-specific components
│   │   │   ├── booking/       # Booking related components
│   │   │   └── chat/          # Chat functionality
│   │   ├── hooks/             # Custom React hooks
│   │   ├── pages/             # Application pages
│   │   │   └── auth/          # Authentication pages
│   │   ├── services/          # External services and API clients
│   │   └── utils/             # Utility functions
│   ├── public/                # Static assets
│   ├── package.json           # Node.js dependencies
│   ├── vite.config.js         # Vite configuration
│   └── eslint.config.js       # ESLint configuration
│
├── README.md                   # Project documentation
└── .gitignore                 # Git ignore rules
```

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21** or higher
- **Node.js 18** or higher
- **npm** or **yarn**
- **Maven 3.6** or higher
- **MySQL 8.0** or higher
- **MongoDB 6.0** or higher
- **Git**

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Hari8901/TourVerse_Application.git
cd TourVerse_Application
```

### 2. Backend Setup

#### Database Configuration
1. **MySQL Setup:**
   - Create a database named `tourverse_db`
   - Update connection details in `application-dev.properties`

2. **MongoDB Setup:**
   - Ensure MongoDB is running on default port (27017)
   - Database will be created automatically

#### Environment Configuration
Create and configure your environment-specific properties files:

**For Development (`application-dev.properties`):**
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/tourverse_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/tourverse_chat

# JWT Configuration
app.jwtSecret=tourverseSecretKey
app.jwtExpirationInMs=86400000

# Server Configuration
server.port=8080
```

**For Production (`application-prod.properties`):**
```properties
# Production database configurations
# Update with your production database URLs and credentials
```

#### Install Dependencies and Build
```bash
cd backend
mvn clean install
```

### 3. Frontend Setup

```bash
cd frontend
npm install
```

#### Environment Configuration (Optional)
Create a `.env` file in the frontend directory for environment variables:
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_WS_BASE_URL=ws://localhost:8080/ws
```

## 🏃‍♂️ Running the Application

### Quick Start (Development Mode)

#### 1. Start Backend
```bash
cd backend
mvn spring-boot:run
```
The backend will start on `http://localhost:8080`

#### 2. Start Frontend
```bash
cd frontend
npm run dev
```
The frontend will start on `http://localhost:5173`

#### 3. Access the Application
- **Frontend**: Open `http://localhost:5173` in your browser
- **Backend API**: Available at `http://localhost:8080/api`
- **API Documentation**: `http://localhost:8080/swagger-ui.html` (when implemented)

### Production Mode

#### Build and Run Backend
```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

#### Build and Run Frontend
```bash
cd frontend
npm run build
npm run preview
```

### Using VS Code Tasks
If you're using VS Code, you can use the configured tasks:
- `Ctrl+Shift+P` → "Tasks: Run Task" → "Start Dev Server" (for frontend)
- Backend can be run through the Spring Boot dashboard extension

## 📚 API Documentation

Once the backend is running, you can access the API documentation at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (when implemented)
- **OpenAPI Docs**: `http://localhost:8080/api-docs` (when implemented)

### Key API Endpoints

#### Authentication
- `POST /api/auth/traveler/login` - Traveler login
- `POST /api/auth/traveler/register/init` - Traveler registration initialization
- `POST /api/auth/traveler/register/verify` - Traveler registration verification
- `POST /api/auth/traveler/logout` - User logout
- `POST /api/auth/traveler/forgot-password` - Password reset request
- `POST /api/auth/traveler/reset-password` - Password reset confirmation

#### Tours
- `GET /api/public/tours` - Get all tours (public)
- `GET /api/public/tours/{id}` - Get tour by ID (public)
- `GET /api/public/tours/search` - Search tours with filters
- `GET /api/public/tours/featured` - Get featured tours

#### Destinations
- `GET /api/public/destinations` - Get all destinations
- `GET /api/public/destinations/{id}` - Get destination by ID
- `GET /api/public/destinations/popular` - Get popular destinations

#### Bookings (Authenticated)
- `GET /api/traveler/bookings` - Get user bookings
- `POST /api/traveler/bookings` - Create new booking
- `GET /api/traveler/bookings/{id}` - Get booking details
- `PUT /api/traveler/bookings/{id}/cancel` - Cancel booking

#### User Profile (Authenticated)
- `GET /api/traveler/profile` - Get user profile
- `PUT /api/traveler/profile/update` - Update user profile

#### Payments (Authenticated)
- `POST /api/payment/process` - Process payment
- `GET /api/traveler/payments` - Get payment history

#### WebSocket Endpoints
- `WebSocket /ws/chat` - Real-time chat endpoint

## 🧪 Testing

### Backend Tests
```bash
cd backend
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=BackendApplicationTests
```

### Frontend Tests
```bash
cd frontend
# Run all tests
npm run test

# Run tests in watch mode
npm run test:watch

# Run tests with coverage
npm run test:coverage
```

### Integration Testing
```bash
# Run backend integration tests
cd backend
mvn verify

# Test API endpoints manually
curl -X GET http://localhost:8080/api/public/tours
```

## � Recent Updates & Improvements

### ✅ **Application Structure Cleanup (Latest)**
- **Organized component architecture** - Moved all components to dedicated files
- **Cleaned up App.jsx** - Now only handles routing, removed inline components
- **Fixed authentication flow** - Added AuthProvider wrapper for proper context
- **Resolved white page issues** - Fixed import paths and component dependencies
- **Enhanced page components** - Added proper content to Tours, Destinations, About, Contact pages
- **Created dedicated auth pages** - LoginPage and RegisterPage with proper forms

### ✅ **Frontend Improvements**
- **Updated to React 19.1** with latest features
- **Bootstrap 5.3 integration** with responsive design
- **Vite 7.0** for faster development builds
- **Context API implementation** for state management
- **Proper error handling** and loading states

### ✅ **Backend Structure**
- **Spring Boot 3.x** with modern Java 21 features
- **Multi-profile configuration** (dev, test, prod)
- **JWT authentication** implementation
- **RESTful API design** with proper endpoint structure

## 🚢 Deployment

### Docker Deployment

#### Build Images
```bash
# Backend
cd backend
docker build -t tourverse-backend .

# Frontend
cd frontend
docker build -t tourverse-frontend .
```

#### Run with Docker Compose
```bash
# Create docker-compose.yml in root directory
docker-compose up -d
```

### Local Development Deployment

#### Using JAR file
```bash
# Build and run backend
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Build and serve frontend
cd frontend
npm run build
npm install -g serve
serve -s dist -l 3000
```

### Cloud Deployment Options

The application is designed to be deployed on various cloud platforms:

#### AWS Deployment
- **EC2** for application hosting
- **RDS** for MySQL database
- **DocumentDB** for MongoDB compatibility
- **S3** for static file storage
- **CloudFront** for CDN

#### Heroku Deployment
- **Heroku Buildpacks** for Node.js and Java
- **ClearDB** for MySQL
- **mLab** for MongoDB

#### Digital Ocean Deployment
- **App Platform** for easy deployment
- **Managed Databases** for MySQL and MongoDB

## � Troubleshooting

### Common Issues

#### Frontend White Page
- **Issue**: Application shows white page on startup
- **Solutions**:
  - Ensure all components are properly exported
  - Check AuthProvider is wrapping the Router
  - Verify Bootstrap CSS is imported in main.jsx
  - Check browser console for errors

#### Backend Connection Issues
- **Issue**: Cannot connect to backend API
- **Solutions**:
  - Ensure backend is running on port 8080
  - Check database connections (MySQL/MongoDB)
  - Verify CORS configuration
  - Check application-dev.properties settings

#### Database Connection
- **Issue**: Database connection failures
- **Solutions**:
  - Verify MySQL/MongoDB services are running
  - Check database credentials in properties files
  - Ensure databases exist and are accessible
  - Check firewall and port settings

### Development Tips

- Use browser developer tools to debug frontend issues
- Check backend logs for API errors
- Use Postman to test API endpoints
- Verify environment variables and configurations

## 👥 Contributing

We welcome contributions to TourVerse! Here's how you can help:

### Getting Started
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

### Development Guidelines

#### Code Standards
- **Backend**: Follow Java coding standards and Spring Boot best practices
- **Frontend**: Use ESLint configuration and React best practices
- **Naming**: Use meaningful variable and function names
- **Comments**: Add comments for complex logic

#### Testing Requirements
- Write unit tests for new features
- Ensure existing tests pass
- Add integration tests for API endpoints
- Test responsive design on different screen sizes

#### Documentation
- Update README.md for new features
- Add API documentation for new endpoints
- Include code comments for complex functions
- Update project structure documentation if needed

#### Pull Request Process
1. Ensure your code builds successfully
2. Run all tests and ensure they pass
3. Update documentation as needed
4. Request review from maintainers
5. Address any feedback promptly

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support & Contact

For support, questions, or contributions:

- 📧 **Email**: support@tourverse.com
- � **Bug Reports**: [GitHub Issues](https://github.com/Hari8901/TourVerse_Application/issues)
- � **Feature Requests**: [GitHub Discussions](https://github.com/Hari8901/TourVerse_Application/discussions)
- 📚 **Documentation**: [Project Wiki](https://github.com/Hari8901/TourVerse_Application/wiki)

### Getting Help

1. **Check the documentation** - Most common issues are covered here
2. **Search existing issues** - Your question might already be answered
3. **Create a detailed issue** - Include steps to reproduce, error messages, and environment details
4. **Join the community** - Connect with other developers and contributors

## 🙏 Acknowledgments

Special thanks to:

- **Spring Boot Team** - For the amazing enterprise framework
- **React Team** - For the powerful and modern UI library
- **Bootstrap Team** - For the comprehensive CSS framework
- **Open Source Community** - For the excellent libraries and tools used in this project
- **Contributors** - Everyone who has contributed to making TourVerse better

## 📊 Project Status

- ✅ **Frontend**: Core structure complete with routing and authentication
- 🔄 **Backend**: API structure in development
- 🔄 **Database**: Schema design and initial setup
- ⏳ **Integration**: Frontend-Backend API integration in progress
- ⏳ **Testing**: Unit and integration tests setup
- ⏳ **Deployment**: CI/CD pipeline setup

---

**Built with ❤️ by the TourVerse Development Team**

*Last Updated: August 2025*