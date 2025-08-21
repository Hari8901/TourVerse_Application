# TourVerse Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0+-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

A comprehensive travel guide booking system built with Spring Boot, providing a platform for travelers to book tours and connect with local guides.

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/Hari8901/TourVerse_Application.git
cd TourVerse_Application/backend

# Option 1: Use setup script (Windows)
setup.bat

# Option 2: Use setup script (Linux/Mac)
chmod +x setup.sh && ./setup.sh

# Option 3: Manual setup
mvn clean install && mvn spring-boot:run
```

Access the application at `http://localhost:8080`

## 🚀 Project Overview

TourVerse is a full-stack travel application that connects travelers with local guides. The backend provides REST APIs for user management, tour package creation, guide booking, real-time chat, payment processing, and review systems.

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Installation](#-installation)
- [Configuration](#️-configuration)
- [API Documentation](#-api-documentation)
- [Database Schema](#️-database-schema)
- [Contributing](#-contributing)

## ✨ Features

### Core Features

- **Multi-role Authentication**: Support for Travelers, Guides, and Admins with JWT-based authentication
- **OTP Verification**: Email-based OTP verification for registration and password reset
- **Tour Package Management**: Create, update, and manage pre-built tour packages
- **Guide Booking System**: Book local guides for custom tours
- **Payment Integration**: Razorpay integration for secure payments
- **Real-time Chat**: WebSocket-based messaging between travelers and guides
- **Review & Rating System**: MongoDB-based review system for guides and travelers
- **Custom Itinerary Creation**: Build personalized travel itineraries
- **File Upload**: AWS S3 integration for document and image uploads
- **Guide Verification**: Admin verification system for guide credentials

### Advanced Features

- **Hybrid Database Architecture**: MySQL for transactional data, MongoDB for chat and reviews
- **Redis Caching**: OTP management and session handling
- **Email Notifications**: Comprehensive email service with templates
- **Security**: JWT tokens, password encryption, role-based access control
- **File Management**: Secure file upload with AWS S3
- **Error Handling**: Global exception handling with custom error responses

## 🛠 Technology Stack

### Framework & Language

- **Java 21** - Programming language
- **Spring Boot 3.5.4** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Data MongoDB** - MongoDB integration
- **Spring WebSocket** - Real-time communication

### Databases

- **MySQL** - Primary relational database
- **MongoDB** - Document store for chat and reviews
- **Redis** - Caching and session management

### External Services

- **AWS S3** - File storage
- **Razorpay** - Payment gateway
- **Gmail SMTP** - Email service

### Build & Tools

- **Maven** - Dependency management
- **Lombok** - Code generation
- **ModelMapper** - Object mapping
- **SpringDoc OpenAPI** - API documentation

## 🏗 Architecture

The application follows a layered architecture with clear separation of concerns:

```text
├── Controller Layer    - REST endpoints and request handling
├── Service Layer       - Business logic implementation
├── Repository Layer    - Data access abstraction
├── Entity/Document     - Data models (JPA entities & MongoDB documents)
├── DTO Layer          - Data transfer objects
├── Configuration      - Application configuration
├── Security           - Authentication and authorization
└── Common             - Shared utilities and exceptions
```

### Key Architectural Decisions

1. **Hybrid Database Strategy**:
   - MySQL for transactional data (users, bookings, payments)
   - MongoDB for flexible data (chat messages, reviews)

2. **JWT Authentication**: Stateless authentication with role-based access control

3. **Microservice-Ready**: Modular package structure for easy service extraction

4. **Event-Driven Communication**: WebSocket for real-time features

## 📁 Project Structure

```text
src/main/java/com/tourverse/backend/
├── BackendApplication.java          # Main application class
├── admin/                           # Admin management
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── auth/                            # Authentication & JWT
│   ├── dto/
│   ├── filter/
│   ├── service/
│   └── util/
├── booking/                         # Booking management
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── chat/                            # Real-time messaging
│   ├── controller/
│   ├── document/        # MongoDB documents
│   ├── dto/
│   ├── repository/
│   └── service/
├── common/                          # Shared components
│   ├── config/         # Configuration classes
│   ├── dto/           # Common DTOs
│   ├── exceptions/    # Exception handling
│   └── util/         # Utility classes
├── guide/                           # Guide management
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── itinerary/                       # Custom itinerary creation
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── payment/                         # Payment processing
│   ├── controller/
│   ├── dto/
│   └── service/
├── review/                          # Review & rating system
│   ├── controller/
│   ├── document/      # MongoDB documents
│   ├── dto/
│   ├── repository/
│   └── service/
├── tourPackage/                     # Tour package management
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── services/
└── user/                            # User management
    ├── controller/
    ├── dto/
    ├── entity/
    ├── repository/
    └── service/
```

## 🚀 Installation

### Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8.0+
- MongoDB 6.0+
- Redis 6.0+
- AWS Account (for S3)
- Razorpay Account

### Setup Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/Hari8901/TourVerse_Application.git
   cd TourVerse_Application/backend
   ```

2. **Configure databases**

   ```sql
   # MySQL
   CREATE DATABASE tourverse_dev;
   
   # MongoDB & Redis will be automatically configured
   ```

3. **Set up environment variables**

   Create `application-dev.properties` in `src/main/resources/` and configure your settings:

   ```bash
   # Copy and update configurations
   cp src/main/resources/application.properties src/main/resources/application-dev.properties
   ```

   **⚠️ Important**: Never commit sensitive information like API keys, passwords, or secrets to the repository. The `.gitignore` file is configured to exclude `application-dev.properties`.

4. **Install dependencies**

   ```bash
   mvn clean install
   ```

5. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## ⚙️ Configuration

### Environment-specific configurations

#### Development (`application-dev.properties`)

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/tourverse_dev
spring.datasource.username=root
spring.datasource.password=your_password

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/tourverse_dev

# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379

# AWS S3 Configuration (Replace with your credentials)
cloud.aws.s3.access-key=your_access_key
cloud.aws.s3.secret-key=your_secret_key
cloud.aws.s3.region=ap-south-1
cloud.aws.s3.bucket-name=your-bucket-name

# Razorpay Configuration (Replace with your credentials)
razorpay.api.key-id=your_key_id
razorpay.api.key-secret=your_key_secret

# Email Configuration (Replace with your credentials)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# JWT Configuration (Use a strong secret in production)
jwt.secret=your_jwt_secret_minimum_32_characters
jwt.expiration-in-ms=3600000
```

### Production Configuration

- Use `application-prod.properties` for production settings
- Set strong JWT secrets
- Configure production database credentials
- Use production AWS S3 buckets

## 📚 API Documentation

### Access Swagger UI

Once the application is running, access the API documentation at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Key API Endpoints

#### Authentication

```http
POST /api/traveler/register/init     - Initialize traveler registration
POST /api/traveler/register/verify   - Complete traveler registration
POST /api/traveler/login/init        - Initialize traveler login
POST /api/traveler/login/verify      - Complete traveler login

POST /api/guide/register/init        - Initialize guide registration
POST /api/guide/register/verify      - Complete guide registration
POST /api/guide/login/init           - Initialize guide login
POST /api/guide/login/verify         - Complete guide login

POST /api/admin/register/init        - Initialize admin registration
POST /api/admin/register/verify      - Complete admin registration
POST /api/admin/login/init           - Initialize admin login
POST /api/admin/login/verify         - Complete admin login
```

#### Tour Packages (Public)

```http
GET  /api/public/tours               - Get all tour packages
GET  /api/public/tours/{id}          - Get tour package by ID
```

#### Tour Packages (Admin)

```http
POST /api/admin/tours                - Create tour package
PUT  /api/admin/tours/{id}           - Update tour package
DELETE /api/admin/tours/{id}         - Delete tour package
```

#### Bookings

```http
POST /api/traveler/bookings/guide    - Create guide booking
GET  /api/traveler/bookings          - Get traveler bookings
POST /api/traveler/bookings/{id}/complete - Complete booking

POST /api/guide/bookings/{id}/accept - Accept booking
GET  /api/guide/bookings/available   - Get available bookings

POST /api/bookings/package           - Create package booking
```

#### Payment

```http
POST /api/payment/order/guide-booking/{id}    - Create payment order for guide booking
POST /api/payment/order/package-booking/{id}  - Create payment order for package booking
```

#### Chat

```http
GET  /api/chat/history/{chatRoomId}  - Get chat history
POST /api/chat/send                  - Send message
WebSocket: /ws/chat                  - Real-time messaging
```

#### Reviews

```http
POST /api/reviews                    - Create review
GET  /api/reviews/user/{userId}      - Get user reviews
```

## 🗄️ Database Schema

### MySQL Tables

#### Users (Base table with inheritance)

```sql
users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  phone VARCHAR(10) NOT NULL,
  password VARCHAR(200) NOT NULL,
  role ENUM('TRAVELER', 'GUIDE', 'ADMIN') NOT NULL,
  is_enabled BOOLEAN DEFAULT TRUE,
  average_rating DOUBLE DEFAULT 0.0,
  rating_count INT DEFAULT 0,
  profile_picture_url VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
```

#### Travelers (Extends Users)

```sql
travelers (
  id BIGINT PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  -- Additional traveler-specific fields
)
```

#### Guides (Extends Users)

```sql
guides (
  id BIGINT PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  aadhaar_no VARCHAR(12) UNIQUE NOT NULL,
  pan_no VARCHAR(10) UNIQUE NOT NULL,
  biography TEXT,
  rate_per_hour DECIMAL(10,2) NOT NULL,
  location VARCHAR(255) NOT NULL,
  verification_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
  aadhaar_document_url VARCHAR(500) NOT NULL,
  pan_document_url VARCHAR(500) NOT NULL,
  guide_certificate_url VARCHAR(500)
)
```

#### Tour Packages

```sql
tour_packages (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  location VARCHAR(255) NOT NULL,
  duration_days INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  image_url VARCHAR(500),
  inclusions TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
```

#### Guide Bookings

```sql
bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  traveler_id BIGINT NOT NULL REFERENCES travelers(id),
  guide_id BIGINT REFERENCES guides(id),
  tour_date DATE NOT NULL,
  tour_time TIME NOT NULL,
  hours INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  location VARCHAR(255) NOT NULL,
  status ENUM('PENDING', 'CONFIRMED', 'CANCELED', 'COMPLETED') DEFAULT 'PENDING',
  payment_status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
  razorpay_order_id VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

### MongoDB Collections

#### Chat Messages

```javascript
{
  _id: ObjectId,
  chatRoomId: String,        // "traveler_guide_id"
  senderId: Long,
  recipientId: Long,
  content: String,
  timestamp: ISODate
}
```

#### User Reviews

```javascript
{
  _id: ObjectId,
  bookingId: Long,           // Reference to MySQL booking
  reviewerId: Long,
  reviewerRole: String,      // TRAVELER or GUIDE
  revieweeId: Long,          // User being reviewed
  rating: Number,            // 1-5
  comment: String,
  createdAt: ISODate
}
```

## 🧪 Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TourPackageServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Configuration

- H2 in-memory database for testing
- Embedded MongoDB for integration tests
- Mock external services (AWS S3, Razorpay, Email)

## 🚀 Deployment

### Building for Production

```bash
# Create production JAR
mvn clean package -Pprod

# The JAR will be created at target/backend-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
```

### Environment Variables for Production

```bash
MYSQL_URL=jdbc:mysql://your-db-host:3306/tourverse_prod
MYSQL_USERNAME=your_username
MYSQL_PASSWORD=your_password
MONGODB_URI=mongodb://your-mongo-host:27017/tourverse_prod
REDIS_HOST=your-redis-host
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
RAZORPAY_KEY_ID=your_key_id
RAZORPAY_KEY_SECRET=your_key_secret
JWT_SECRET=your_strong_jwt_secret
MAIL_USERNAME=your_email
MAIL_PASSWORD=your_app_password
```

## 🤝 Contributing

We welcome contributions to TourVerse! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Write meaningful commit messages
- Add JavaDoc for public methods
- Include unit tests for new features

### Development Guidelines

- Ensure all tests pass before submitting a PR
- Update documentation for any new features
- Follow the existing project structure
- Use meaningful variable and method names
- Handle exceptions appropriately

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Hari** - Backend Developer & Architecture
- **Team TourVerse** - Full Stack Development

## 📞 Support

For support and queries:

- Email: [avhadhari7@gmail.com](mailto:avhadhari7@gmail.com)
- GitHub Issues: [Create an issue](https://github.com/Hari8901/TourVerse_Application/issues)

## 🔗 Related Projects

- [TourVerse Frontend](../frontend) - React.js frontend application
- [TourVerse Mobile App](https://github.com/Hari8901/TourVerse_Mobile) - React Native mobile application (if applicable)

## 📈 Roadmap

- [ ] Integration with more payment gateways
- [ ] Advanced search and filtering
- [ ] Mobile app development
- [ ] AI-powered recommendations
- [ ] Multi-language support
- [ ] Advanced analytics dashboard

---

Happy Traveling! 🌍✈️
