#!/bin/bash

echo "====================================="
echo "   TourVerse Backend Setup Script"
echo "====================================="
echo

echo "Creating development properties file..."
if [ ! -f "src/main/resources/application-dev.properties" ]; then
    cp "src/main/resources/application.properties" "src/main/resources/application-dev.properties"
    echo "application-dev.properties created successfully!"
    echo
    echo "IMPORTANT: Please update the following configurations in application-dev.properties:"
    echo "- Database credentials (MySQL, MongoDB, Redis)"
    echo "- AWS S3 credentials"
    echo "- Razorpay API keys"
    echo "- Email configuration"
    echo "- JWT secret key"
    echo
else
    echo "application-dev.properties already exists!"
    echo
fi

echo "Installing Maven dependencies..."
mvn clean install -q

if [ $? -eq 0 ]; then
    echo
    echo "====================================="
    echo "   Setup completed successfully!"
    echo "====================================="
    echo
    echo "To start the application, run:"
    echo "  mvn spring-boot:run"
    echo
    echo "Access the application at:"
    echo "  http://localhost:8080"
    echo
    echo "API Documentation:"
    echo "  http://localhost:8080/swagger-ui.html"
    echo
else
    echo
    echo "====================================="
    echo "   Setup failed!"
    echo "====================================="
    echo "Please check the error messages above."
    echo
fi
