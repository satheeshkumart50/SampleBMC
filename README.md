📘 BookMyConsultation (BMC)

BookMyConsultation is a cloud-native, microservices-based healthcare appointment platform that enables users to discover doctors, book appointments, make payments, provide ratings, and receive notifications. The system is built using an event-driven architecture with Apache Kafka, MongoDB, and MySQL, orchestrated via Docker Compose, and integrated with AWS services such as EC2, S3, SES, and RDS for scalable deployment, secure storage, and reliable notifications.

This project demonstrates enterprise-grade microservices design, asynchronous messaging, JWT-based security, and cloud-native deployment patterns.

🏗️ Architecture Overview

    • Microservices architecture with clear domain boundaries
    • Event-driven communication using Apache Kafka
    • API Gateway for centralized routing
    • JWT-based authentication and authorization
    • Docker Compose for local orchestration
    • AWS integrations (EC2, S3, SES, RDS)

🧩 Microservices Breakdown
1️⃣ Doctor Service

    Manages doctor onboarding and lifecycle
    
    Responsibilities
    • Doctor registration and approval/rejection
    • Upload and download doctor documents (AWS S3)
    • Publish Kafka events on approval/rejection
    • Consume rating updates from Kafka

    Key Features
    • Role-based security (Admin / User)
    • MongoDB persistence
    • Kafka producer and consumer
    • In-memory caching for faster access

2️⃣ User Service

    Handles patient onboarding and verification
    
    Responsibilities
    • User registration
    • Trigger verification email via Kafka
    • Upload and download user documents (AWS S3)
    
    Tech Stack
    • MongoDB
    • Kafka Producer
    • JWT-based security

3️⃣ Appointment Service

    Core booking and scheduling engine
    
    Responsibilities
    • Publish doctor availability
    • Book appointments
    • Generate prescriptions
    • Trigger Kafka events for confirmations and prescriptions
    
    Tech Stack
    • MySQL (appointments and availability)
    • MongoDB (prescriptions)
    • Feign clients (Doctor & User validation)
    • Kafka Producer

4️⃣ Payment Service

    Handles appointment payments
    
    Responsibilities
    • Process payments
    • Update appointment status
    • Publish payment confirmation events
    
    Tech Stack
    • MongoDB
    • Kafka Producer
    • Inter-service communication

5️⃣ Rating Service

    Collects and propagates doctor ratings
    
    Responsibilities
    • Accept user ratings
    • Publish rating events
    • Update doctor ratings via Doctor Service
    
    Tech Stack
    • MongoDB
    • Kafka Producer
    • Feign Client

6️⃣ Security Service

    Centralized authentication and authorization
    
    Responsibilities
    • Generate JWT tokens
    • Role and permission management
    • Secure access to all microservices
    
    Tech Stack
    • Spring Security
    • JWT authentication and authorization

7️⃣ Notification Service

    Asynchronous email notification engine
    
    Responsibilities
    • Consume Kafka events
    • Send emails to users and doctors
    • Apply Freemarker email templates
    
    Tech Stack
    • Kafka Consumer
    • AWS SES
    • Freemarker

8️⃣ BMC Gateway

    API Gateway for the entire system
    
    Responsibilities
    • Route traffic to appropriate microservices
    • Act as a centralized entry point
    
    Tech Stack
    • Spring Cloud Gateway
