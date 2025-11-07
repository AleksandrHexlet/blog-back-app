Blog Backend Application - это REST API бэкенд для приложения-блога, разработанный на Java 21 с использованием Spring Framework 6.1+. Приложение работает в сервлет-контейнере Tomcat 10 и использует реляционную базу данных PostgreSQL (или встроенную H2 для тестирования).

✨ Основные особенности
Архитектура: Трехслойная архитектура (Controller → Service → DAO/Repository)

REST API: Полнофункциональный REST API для управления постами и комментариями

Spring Data JDBC: Использование Spring Data JDBC для работы с БД вместо ORM

Тестирование:  JUnit 5, Spring Test Framework и контекстным кешированием

Валидация: Встроенная валидация данных с использованием Jakarta Validation

Документация: Полный JAVADOC для всех классов и методов

Развертывание: Упакован как WAR файл для развертывания на Tomcat

🏗️ Архитектура приложения
text
┌─────────────────┐
│   Браузер       │ (http://localhost/80)
└────────┬────────┘
│ HTTP запросы
▼
┌─────────────────┐
│  Nginx Server   │ (Frontend)
└────────┬────────┘
│ REST API запросы
▼
┌───────────────────────────────────────┐
│   Tomcat Servlet Container            │ (http://localhost:8080)
│  ┌─────────────────────────────────┐  │
│  │   Spring MVC Controller Layer   │  │
│  │  (REST Endpoints)               │  │
│  └──────────────┬──────────────────┘  │
│                 │                      │
│  ┌──────────────▼──────────────────┐  │
│  │   Service Layer                 │  │
│  │  (Business Logic)               │  │
│  └──────────────┬──────────────────┘  │
│                 │                      │
│  ┌──────────────▼──────────────────┐  │
│  │   DAO/Repository Layer          │  │
│  │  (Spring Data JDBC)             │  │
│  └──────────────┬──────────────────┘  │
└─────────────────┼─────────────────────┘
│ SQL запросы
▼
┌─────────────────┐
│  PostgreSQL DB  │
│  (или H2)       │
└─────────────────┘
📁 Структура проекта
text
blog-back-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ru/yandex/practicum/blog/
│   │   │       ├── config/                    # Spring конфигурация
│   │   │       │   ├── AppConfig.java         # Основная конфигурация приложения
│   │   │       │   ├── WebConfig.java         # Web MVC конфигурация
│   │   │       │   └── DataSourceConfig.java  # Конфигурация DataSource
│   │   │       ├── controller/                # REST контроллеры
│   │   │       │   ├── PostController.java
│   │   │       │   └── CommentController.java
│   │   │       ├── service/                   # Бизнес-логика
│   │   │       │   ├── PostService.java
│   │   │       │   ├── CommentService.java
│   │   │       │   └── ImageService.java
│   │   │       ├── repository/                # Spring Data JDBC репозитории
│   │   │       │   ├── PostRepository.java
│   │   │       │   ├── CommentRepository.java
│   │   │       │   ├── PostTagRepository.java
│   │   │       │   └── ImageRepository.java
│   │   │       ├── model/                     # Модели данных
│   │   │       │   ├── Post.java
│   │   │       │   ├── Comment.java
│   │   │       │   ├── PostTag.java
│   │   │       │   └── Image.java
│   │   │       ├── dto/                       # Data Transfer Objects
│   │   │       │   ├── PostDTO.java
│   │   │       │   ├── PostListResponseDTO.java
│   │   │       │   ├── CommentDTO.java
│   │   │       │   └── ErrorResponseDTO.java
│   │   │       ├── exception/                 # Custom exceptions
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   ├── InvalidInputException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── util/                      # Вспомогательные классы
│   │   │       │   ├── TextUtil.java
│   │   │       │   └── FileUtil.java
│   │   │       └── web/
│   │   │           └── WebApplicationInitializer.java  # Инициализатор Servlet контейнера
│   │   └── resources/
│   │       ├── application.properties          # Конфигурация приложения
│   │       ├── application-postgres.properties # PostgreSQL профиль
│   │       ├── application-h2.properties       # H2 профиль
│   │       ├── logback.xml                     # Логирование
│   │       ├── schema.sql                      # Схема БД
│   │       └── data.sql                        # Начальные данные
│   └── test/
│       ├── java/
│       │   └── ru/yandex/practicum/blog/
│       │       ├── controller/
│       │       │   ├── PostControllerTest.java
│       │       │   └── CommentControllerTest.java
│       │       ├── service/
│       │       │   ├── PostServiceTest.java
│       │       │   └── CommentServiceTest.java
│       │       ├── repository/
│       │       │   ├── PostRepositoryTest.java
│       │       │   └── CommentRepositoryTest.java
│       │       ├── integration/
│       │       │   ├── PostIntegrationTest.java
│       │       │   └── CommentIntegrationTest.java
│       │       └── config/
│       │           └── TestDataSourceConfig.java
│       └── resources/
│           ├── application-test.properties
│           ├── schema-test.sql
│           └── data-test.sql
├── pom.xml                                     # Maven конфигурация
├── .gitignore                                  # Git ignore файл
├── README.md                                   # Этот файл
└── DEPLOYMENT.md                               # Гайд по развертыванию

🚀 Быстрый старт
Требования
Java 21+: Убедитесь, что установлена JDK 21 или выше

Maven 3.8+: Для сборки проекта

PostgreSQL 14+: Для использования в production (опционально)

Git: Для управления версиями

Установка и сборка
Клонируйте репозиторий:

bash
git clone https://github.com/AleksandrHexlet/blog-back-app.git
cd blog-back-app
Создайте feature ветку:

bash
git checkout -b feature/initial-setup
Соберите проект:

bash
mvn clean install
Запустите тесты:

bash
mvn test
Создайте WAR файл:

bash
mvn clean package
WAR файл будет доступен по пути: target/blog-back-app.war

📊 REST API Endpoints
Posts API
1. Получение списка постов (пагинация, поиск, фильтрация)
   text
   GET /api/posts?search=Lalala&pageNumber=1&pageSize=5
   Параметры запроса:

search (required): Строка поиска по названию и тексту поста

pageNumber (required): Номер страницы (начиная с 1)

pageSize (required): Количество постов на странице

Ответ 200 OK:

json
{
"posts": [
{
"id": 1,
"title": "My First Post",
"text": "This is the text of my first post...",
"tags": ["java", "spring"],
"likesCount": 5,
"commentsCount": 2
}
],
"hasPrev": false,
"hasNext": true,
"lastPage": 3
}
2. Получение поста по ID
   text
   GET /api/posts/{id}
   Параметры пути:

id (required): ID поста

Ответ 200 OK:

json
{
"id": 1,
"title": "My First Post",
"text": "This is the full text of my first post without truncation...",
"tags": ["java", "spring"],
"likesCount": 5,
"commentsCount": 2
}
3. Создание нового поста
   text
   POST /api/posts
   Content-Type: application/json
   Тело запроса:

json
{
"title": "New Post Title",
"text": "Post text in Markdown format...",
"tags": ["tag1", "tag2"]
}
Ответ 201 Created:

json
{
"id": 3,
"title": "New Post Title",
"text": "Post text in Markdown format...",
"tags": ["tag1", "tag2"],
"likesCount": 0,
"commentsCount": 0
}
4. Редактирование поста
   text
   PUT /api/posts/{id}
   Content-Type: application/json
   Тело запроса:

json
{
"id": 1,
"title": "Updated Title",
"text": "Updated text...",
"tags": ["updated-tag"]
}
Ответ 200 OK: Обновленный пост

5. Удаление поста
   text
   DELETE /api/posts/{id}
   Ответ 204 No Content: Пост успешно удален

6. Инкремент лайков
   text
   POST /api/posts/{id}/likes
   Ответ 200 OK:

json
6
7. Загрузка картинки поста
   text
   PUT /api/posts/{id}/image
   Content-Type: multipart/form-data
   Параметры:

image (form-data): Файл изображения

Ответ 200 OK: Картинка успешно загружена

8. Получение картинки поста
   text
   GET /api/posts/{id}/image
   Ответ 200 OK: Байты изображения

Comments API
1. Получение комментариев поста
   text
   GET /api/posts/{postId}/comments
   Ответ 200 OK:

json
[
{
"id": 1,
"text": "Great post!",
"postId": 1
}
]
2. Получение комментария
   text
   GET /api/posts/{postId}/comments/{commentId}
   Ответ 200 OK:

json
{
"id": 1,
"text": "Great post!",
"postId": 1
}
3. Создание комментария
   text
   POST /api/posts/{postId}/comments
   Content-Type: application/json
   Тело запроса:

json
{
"text": "This is a great post!",
"postId": 1
}
Ответ 201 Created: Созданный комментарий

4. Редактирование комментария
   text
   PUT /api/posts/{postId}/comments/{commentId}
   Content-Type: application/json
   Тело запроса:

json
{
"id": 1,
"text": "Updated comment text",
"postId": 1
}
Ответ 200 OK: Обновленный комментарий

5. Удаление комментария
   text
   DELETE /api/posts/{postId}/comments/{commentId}
   Ответ 204 No Content: Комментарий успешно удален

🗄️ Структура базы данных
Таблица posts
sql
CREATE TABLE posts (
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
text TEXT NOT NULL,
likes_count INTEGER DEFAULT 0,
comments_count INTEGER DEFAULT 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Таблица post_tags
sql
CREATE TABLE post_tags (
id BIGSERIAL PRIMARY KEY,
post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
tag_name VARCHAR(100) NOT NULL
);
Таблица comments
sql
CREATE TABLE comments (
id BIGSERIAL PRIMARY KEY,
post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
text TEXT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Таблица images
sql
CREATE TABLE images (
id BIGSERIAL PRIMARY KEY,
post_id BIGINT NOT NULL UNIQUE REFERENCES posts(id) ON DELETE CASCADE,
data BYTEA NOT NULL,
content_type VARCHAR(50),
file_name VARCHAR(255)
);
🧪 Тестирование
Запуск всех тестов
bash
mvn test
Запуск конкретного теста
bash
mvn test -Dtest=PostServiceTest
Запуск тестов с покрытием
bash
mvn test jacoco:report
Типы тестов
Unit Tests (Юнит тесты)
Тестируют отдельные компоненты в изоляции:

PostServiceTest: Тесты бизнес-логики сервиса постов

CommentServiceTest: Тесты бизнес-логики сервиса комментариев

Пример:

java
@Test
void testCreatePost_ValidInput_ReturnsPost() {
PostDTO input = new PostDTO("Title", "Text", Arrays.asList("tag1"));
PostDTO result = postService.createPost(input);

    assertNotNull(result.getId());
    assertEquals("Title", result.getTitle());
}
Integration Tests (Интеграционные тесты)
Тестируют взаимодействие между компонентами:

PostControllerTest: Тесты REST API контроллера

PostRepositoryTest: Тесты работы с БД

🔧 Конфигурация
application.properties (По умолчанию для H2)
text
# Server
server.port=8080
server.servlet.context-path=/

# Logging
logging.level.root=INFO
logging.level.ru.yandex.practicum.blog=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.data=DEBUG

# Database - H2
spring.datasource.url=jdbc:h2:mem:blog
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=false

# Spring Data JDBC
spring.data.jdbc.repositories.enabled=true
application-postgres.properties (Production с PostgreSQL)
text
# Database - PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/blog
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=postgres

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
📦 Развертывание
Локальное тестирование с H2
Соберите проект:

bash
mvn clean package
Запустите на Tomcat (если установлен локально):

bash
cp target/blog-back-app.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh

bash
curl http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=10
Production развертывание с PostgreSQL
Подробный гайд см. в DEPLOYMENT.md

💻 Стек технологий
Компонент	Версия	Назначение
Java	21	Язык программирования
Spring Framework	6.1.2	Основной фреймворк
Spring Data JDBC	3.2.0	Работа с БД
PostgreSQL	14+	Production БД
H2	2.2.224	In-memory БД для тестов
Maven	3.8+	Сборка проекта
JUnit 5	5.9.3	Unit тестирование
Mockito	5.3.1	Мокирование объектов
Tomcat	10	Servlet контейнер
Lombok	1.18.30	Reduction boilerplate кода


