📘 BookMyConsultation (BMC)

BookMyConsultation is a cloud-native, microservices-based healthcare appointment platform that enables users to discover doctors, book appointments, make payments, provide ratings, and receive notifications. The system is built using an event-driven architecture with Apache Kafka, MongoDB and MySQL, and Docker Compose, and integrates AWS services such as EC2, S3, SES, and RDS to support scalable deployment, secure document storage, and reliable email notifications.

This project demonstrates enterprise-grade microservices design, asynchronous messaging, JWT-based security, and scalable cloud deployment patterns.

🏗️ Architecture Overview
    Microservices Architecture with clear domain boundaries
    Event-Driven Communication using Apache Kafka
    API Gateway for centralized routing
    JWT-based Authentication & Authorization
    Docker Compose for local orchestration
    AWS Integrations (S3, SES, EC2, RDS)

🧩 Microservices Breakdown

1️⃣ Doctor Service
    Manages doctor onboarding and lifecycle.
    Responsibilities
    Doctor registration and approval/rejection
    Upload/download doctor documents (AWS S3)
    Publish Kafka events on approval/rejection
    Consume rating updates from Kafka
    Key Features
    Role-based security (Admin / User)
    MongoDB persistence
    Kafka Producer & Consumer
    In-memory caching for faster access

2️⃣ User Service
    Handles patient onboarding and verification.
    Responsibilities
    User registration
    Verification email via Kafka
    Document upload/download to S3
    Tech: 
    MongoDB
    Kafka Producer
    JWT Security

3️⃣ Appointment Service
    Core booking and scheduling engine.
    Responsibilities
    Publish doctor availability
    Book appointments
    Generate prescriptions
    Trigger Kafka events for confirmations
    Tech:
    MySQL (Appointments & Availability)
    MongoDB (Prescriptions)
    Feign Clients (Doctor & User validation)
    Kafka Producer

4️⃣ Payment Service
    Handles appointment payments.
    Responsibilities
    Process payments
    Update appointment status
    Publish payment confirmation events
    Tech:
    MongoDB
    Kafka Producer
    Inter-service communication

5️⃣ Rating Service
    Collects and propagates doctor ratings.
    Responsibilities
    Accept user ratings
    Publish rating events
    Update doctor ratings via Doctor Service
    Tech:
    MongoDB
    Kafka Producer
    Feign Client

6️⃣ Security Service
    Centralized authentication service.
    Responsibilities
    Generate JWT tokens
    Role & permission management
    Secure access to all services
    Tech:
    Spring Security
    JWT Authentication & Authorization

7️⃣ Notification Service
    Asynchronous email notification engine.
    Responsibilities
    Consume Kafka events
    Send emails to users and doctors
    Apply Freemarker email templates
    Tech:
    Kafka Consumer
    AWS SES
    Freemarker

8️⃣ BMC Gateway
    API Gateway for the entire system.
    Responsibilities
    Route traffic to appropriate microservices
    Centralized entry point
    Tech:
    Spring Cloud Gateway

🔧 Technology Stack
    Backend: Java, Spring Boot, Spring Cloud
    Messaging: Apache Kafka
    Databases: MongoDB, MySQL (AWS RDS)
    Security: JWT, Spring Security
    Cloud: AWS EC2, S3, SES
    Containers: Docker, Docker Compose
    Build: Maven
