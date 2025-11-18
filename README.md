# Blog Backend Application

## Overview
Spring Framework 6.1 REST API for Blog application with Spring Data JDBC.

## Technology Stack
- **Java 21**
- **Spring Framework 6.1**
- **Spring Data JDBC** (NOT JPA)
- **Tomcat 10 Servlet Container**
- **H2 Embedded Database**
- **Maven 3.8+**
- **JUnit 5**
- **Mockito**

## Project Structure
src/main/java/ru/yandex/practicum/
├── model/ (Post, Comment)
├── dao/ (PostDao, CommentDao)
├── dto/ (PostDto, CommentDto)
├── service/ (PostService, CommentService, ImageService)
└── controller/ (PostController, CommentController)


## Build & Run

### Prerequisites
- JDK 21+
- Maven 3.8+

### Build Project
```bash
mvn clean package

Run Tests
mvn clean test

Deploy to Tomcat
cp target/blog-back-app.war $TOMCAT_HOME/webapps/ROOT.war
$TOMCAT_HOME/bin/startup.sh

Access Application
http://localhost:8080/api/posts

### API Endpoints
Posts
GET /api/posts?search=&pageNumber=1&pageSize=5

GET /api/posts/{id}

POST /api/posts

PUT /api/posts/{id}

DELETE /api/posts/{id}

POST /api/posts/{id}/likes

PUT /api/posts/{id}/image

GET /api/posts/{id}/image

Comments
GET /api/posts/{postId}/comments

GET /api/posts/{postId}/comments/{commentId}

POST /api/posts/{postId}/comments

PUT /api/posts/{postId}/comments/{commentId}

DELETE /api/posts/{postId}/comments/{commentId}

Database
H2 Embedded Database - In-Memory
Schema automatically created on startup
