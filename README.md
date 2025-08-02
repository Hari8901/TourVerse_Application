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
- **React 19.1** - Modern UI library with hooks
- **Vite** - Fast build tool and development server
- **React Router** - Client-side routing
- **Bootstrap 5** - CSS framework for responsive design
- **Material-UI** - Component library
- **Axios** - HTTP client for API calls
- **WebSocket** - Real-time communication
- **Formik** - Form management and validation

### Backend
- **Spring Boot 3.5.4** - Enterprise Java framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring WebSocket** - Real-time communication
- **JWT** - Token-based authentication
- **Maven** - Dependency management
- **Java 21** - Programming language

### Databases
- **MySQL** - Primary relational database for structured data
- **MongoDB** - Document database for chat messages and reviews

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
│   ├── src/main/java/com/tourverse/backend/
│   │   ├── auth/              # Authentication & authorization
│   │   ├── booking/           # Booking management
│   │   ├── chat/              # Real-time chat functionality
│   │   ├── common/            # Shared utilities and config
│   │   ├── guide/             # Tour guide management
│   │   ├── payment/           # Payment processing
│   │   ├── review/            # Review and rating system
│   │   ├── tour/              # Tour management
│   │   └── user/              # User management
│   ├── src/main/resources/    # Configuration files
│   └── pom.xml                # Maven dependencies
│
├── frontend/                   # React frontend application
│   ├── src/
│   │   ├── api/               # API service layer
│   │   ├── components/        # Reusable UI components
│   │   ├── context/           # React context providers
│   │   ├── features/          # Feature-specific components
│   │   ├── hooks/             # Custom React hooks
│   │   ├── pages/             # Application pages
│   │   ├── services/          # External services
│   │   └── utils/             # Utility functions
│   ├── public/                # Static assets
│   └── package.json           # Node.js dependencies
│
└── README.md                   # Project documentation
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
```bash
cd backend
cp src/main/resources/application.properties.example src/main/resources/application-dev.properties
```

Update the configuration files with your database credentials and API keys:
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/tourverse_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/tourverse_chat

# AWS Configuration (Optional)
cloud.aws.credentials.access-key=your_access_key
cloud.aws.credentials.secret-key=your_secret_key
```

#### Install Dependencies
```bash
mvn clean install
```

### 3. Frontend Setup

```bash
cd frontend
npm install
```

## 🏃‍♂️ Running the Application

### Development Mode

#### Start Backend
```bash
cd backend
mvn spring-boot:run
```
The backend will start on `http://localhost:8080`

#### Start Frontend
```bash
cd frontend
npm run dev
```
The frontend will start on `http://localhost:5173`

### Production Mode

#### Build Backend
```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

#### Build Frontend
```bash
cd frontend
npm run build
npm run preview
```

## 📚 API Documentation

Once the backend is running, you can access the API documentation at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/api-docs`

### Key API Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

#### Tours
- `GET /api/tours` - Get all tours
- `GET /api/tours/{id}` - Get tour by ID
- `POST /api/tours` - Create new tour

#### Bookings
- `GET /api/bookings` - Get user bookings
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}` - Update booking

#### Chat
- `WebSocket /ws/chat` - Real-time chat endpoint

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm run test
```

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
docker-compose up -d
```

### Kubernetes Deployment

```bash
# Apply Kubernetes manifests
kubectl apply -f infra/kubernetes/
```

### Cloud Deployment

The application is configured for deployment on AWS using:
- **EKS** for container orchestration
- **RDS** for MySQL database
- **DocumentDB** for MongoDB compatibility
- **S3** for static file storage

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding standards for backend
- Use ESLint configuration for frontend
- Write unit tests for new features
- Update documentation for API changes

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- 📧 Email: support@tourverse.com
- 💬 Discord: [TourVerse Community](https://discord.gg/tourverse)
- 📋 Issues: [GitHub Issues](https://github.com/Hari8901/TourVerse_Application/issues)

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- React team for the powerful UI library
- Open source community for the excellent libraries used

---

**Built with ❤️ by the TourVerse Team**