# BH LearnSphere - AI-Powered Learning Management System

## Overview

BH LearnSphere is a next-generation Learning Management System (LMS) that goes beyond traditional course delivery. This intelligent, scalable, and engaging platform incorporates AI-driven features to create personalized learning paths for students while providing powerful tools for instructors and administrators.

## Key Features

### Core Learning Management
- **Multi-role Dashboards** - Customized interfaces for Students, Instructors, and Administrators
- **Course Creation & Management** - Rich content support (videos, PDFs, text, external links)
- **Drip Content Scheduling** - Release course materials over time
- **Progress Tracking** - Real-time student progress monitoring
- **Assessment System** - Quizzes with auto-grading and multiple question types

### AI-Powered Intelligence
- **Personalized Learning Paths** - Adaptive content based on student performance
- **AI Course Recommendations** - Smart course suggestions using machine learning
- **Skill Gap Analysis** - Identify and address learning weaknesses
- **Predictive Analytics** - Forecast student performance and engagement

### Engagement & Gamification
- **Badges & Points System** - Reward achievements and participation
- **Progress Visualization** - Interactive progress tracking
- **Leaderboards** - Foster healthy competition
- **Achievement System** - Motivate continuous learning

### Collaboration Tools
- **Real-time Chat** - Course-specific chat rooms
- **Discussion Forums** - Structured Q&A and discussions
- **Live Sessions** - Virtual classroom capabilities
- **Peer Review System** - Collaborative assignment feedback

### Advanced Analytics
- **Student Performance Analytics** - Detailed progress insights
- **Course Engagement Metrics** - Instructor dashboard analytics
- **Completion Rate Tracking** - Monitor course success rates
- **Learning Pattern Analysis** - Identify effective teaching methods

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)

### AI/ML Integration
- **Machine Learning**: Weka Library
- **Recommendation Engine**: Content-based + Collaborative filtering
- **Analytics**: Custom algorithms for learning pattern analysis

### Real-time Features
- **WebSocket**: Spring WebSocket + STOMP
- **Real-time Updates**: Server-sent events for progress tracking

### Additional Technologies
- **File Storage**: Local file system with multipart uploads
- **Caching**: Redis (optional)
- **Email**: Spring Mail for notifications
- **Validation**: Bean Validation API
- **Documentation**: OpenAPI/Swagger (ready for integration)

## Quick Start

### Prerequisites
- Java 17 or later
- PostgreSQL 13 or later
- Maven 3.6 or later
- (Optional) Redis 6+ for caching

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-org/learnsphere.git
   cd learnsphere
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE learnsphere;
   -- The application will auto-create tables on first run
   ```

3. **Configuration**
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/learnsphere
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build the Application**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

### Docker Deployment

1. **Using Docker Compose**
   ```bash
   docker-compose up -d
   ```

2. **Manual Docker Build**
   ```bash
   docker build -t learnsphere .
   docker run -p 8080:8080 learnsphere
   ```

## API Documentation

### Authentication Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/api/auth/register` | User registration | Public |
| POST | `/api/auth/login` | User login | Public |
| POST | `/api/auth/refresh` | Refresh JWT token | Authenticated |

### Course Management
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/courses/public` | Get published courses | Public |
| GET | `/api/courses/{id}` | Get course details | Public |
| POST | `/api/courses` | Create course | Instructor |
| PUT | `/api/courses/{id}` | Update course | Instructor |
| POST | `/api/courses/{id}/enroll` | Enroll in course | Student |

### Student Features
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/student/enrollments` | Get student enrollments | Student |
| POST | `/api/student/progress` | Update lesson progress | Student |
| GET | `/api/student/recommendations` | Get AI recommendations | Student |
| GET | `/api/student/dashboard` | Student dashboard | Student |

### Assessment System
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/api/quiz` | Create quiz | Instructor |
| POST | `/api/quiz/{id}/questions` | Add questions | Instructor |
| POST | `/api/quiz/submit` | Submit quiz | Student |
| GET | `/api/quiz/{id}/results` | Get quiz results | Student/Instructor |

### Analytics & AI
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/api/analytics/course/{id}` | Course analytics | Instructor |
| GET | `/api/ai/learning-path` | Personalized learning path | Student |
| GET | `/api/ai/skill-analysis` | Skill gap analysis | Student |

## Project Structure

```
learnsphere/
├── src/main/java/com/bh/learnsphere/
│   ├── config/              # Configuration classes
│   ├── model/              # JPA entities
│   ├── repository/         # Data access layer
│   ├── service/           # Business logic
│   ├── controller/        # REST API endpoints
│   ├── dto/              # Data transfer objects
│   ├── security/         # Security configuration
│   └── exception/        # Exception handling
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
└── docker/
    ├── Dockerfile
    └── docker-compose.yml
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
