Blog Backend Application - Java 21 & Spring Boot 3.2
Современное REST API приложение-блог на Java 21 и Spring Boot 3.2 с PostgreSQL, Gradle и Spring Data JDBC

📚 Оглавление
Общее описание

Технологический стек

Требования к системе

Быстрый старт

План миграции на Java 21

Архитектура проекта

Java 21 features в проекте

Структура кода

API Endpoints

Сборка и запуск

Тестирование

Конфигурация

Troubleshooting

Общее описание
Это полнофункциональное REST API для блог-платформы, разработанное с использованием современных Java 21 features (record types, sealed classes, pattern matching) и Spring Boot 3.2.

Ключевые характеристики:

✅ Java 21 record types для entities и DTOs

✅ Spring Data JDBC для простого доступа к БД

✅ PostgreSQL как production-ready БД

✅ Gradle для быстрой и гибкой сборки

✅ Comprehensive JUnit 5 тесты с кешированием контекстов

✅ Встроенный Tomcat (Executable JAR)

✅ REST API с полной CRUD функциональностью для постов и комментариев

✅ Пагинация, поиск, загрузка изображений

Технологический стек
Компонент	Версия	Назначение
Java	21+	Язык программирования с record types
Spring Boot	3.2.5+	Фреймворк приложения
Spring Data JDBC	3.2+	Data Access Layer вместо boilerplate DAO
PostgreSQL	15+	Production-ready реляционная БД
Gradle	8+	Система сборки с Kotlin DSL
JUnit 5	5.9+	Тестирование с кешированием контекстов
Spring Boot Test	3.2+	Integration и WebMvc тесты
Lombok	1.18+	Генерация boilerplate (только для логирования)
Jackson	2.15+	JSON сериализация/десериализация
H2	2.1+	In-memory БД для тестов
Требования к системе
Обязательные
Java 21+ (LTS версия с длительной поддержкой)

Gradle 8+ (или использовать gradle wrapper из проекта)

PostgreSQL 15+ (может быть установлен локально или через Docker)

Git для работы с версионированием

Опциональные (рекомендуется)
Docker & Docker Compose (для быстрого запуска PostgreSQL)

IntelliJ IDEA (Ultimate или Community Edition)

Postman или curl (для тестирования API)

Visual Studio Code с Java расширениями

Минимальные ресурсы
ОЗУ: 2+ GB (для работы приложения и БД)

Место на диске: 500+ MB (для Gradle кеша и PostgreSQL данных)

Быстрый старт
1. Клонирование и подготовка
   bash
# Клонировать репозиторий
git clone <repository-url>
cd blog-backend-java21

# Проверить версию Java
java -version  # Должна быть Java 21+
2. Запуск PostgreSQL через Docker
   Самый простой способ запустить PostgreSQL:

bash
# Запустить PostgreSQL в Docker Compose
docker-compose up -d

# Проверить что контейнер запущен
docker-compose ps

# PostgreSQL будет доступен на localhost:5432
# БД: blog_db
# Пользователь: postgres
# Пароль: postgres
Если PostgreSQL уже установлен локально:

bash
# Убедитесь что PostgreSQL запущен
psql -h localhost -U postgres -d postgres

# Создать БД и пользователя если нужно
CREATE DATABASE blog_db;
3. Сборка проекта
   bash
# Сборка с использованием Gradle wrapper
./gradlew build

# Или если gradle установлен глобально
gradle build

# На Windows
gradlew.bat build
4. Запуск приложения
   Вариант 1: Через Gradle

bash
./gradlew bootRun
Вариант 2: Через JAR

bash
# Сначала собрать
./gradlew bootJar

# Потом запустить
java -jar build/libs/blog-application-java21.jar
Вариант 3: С переменными окружения

bash
# Запустить с другим портом
SERVER_PORT=9090 ./gradlew bootRun

# Запустить с другой БД
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/other_db ./gradlew bootRun
5. Проверка что приложение запущено
   bash
# Должна вернуться пустой список постов (или тестовые данные)
curl http://localhost:8080/api/posts

# Ожидаемый ответ:
# {"posts":[],"hasPrev":false,"hasNext":false,"lastPage":1}
6. Попробовать API
   bash
# Создать новый пост
curl -X POST "http://localhost:8080/api/posts?title=Hello&text=My%20first%20post&tags=Java,Spring"

# Получить все посты
curl http://localhost:8080/api/posts

# Получить конкретный пост
curl http://localhost:8080/api/posts/1

# Добавить лайк
curl -X POST http://localhost:8080/api/posts/1/likes

# Создать комментарий
curl -X POST "http://localhost:8080/api/posts/1/comments?text=Great%20post"
План миграции на Java 21
Полный план из 27 микрокоммитов представлен в файле JAVA21_MIGRATION_PLAN.txt

Фазы миграции
Фаза	Название	Коммитов	Описание
1	Инициализация	4	Gradle setup и конфигурация
2	Модели	3	Переписать entities на Java 21 records
3	DTOs	4	Переписать DTOs на records
4	Repositories	3	Создать Spring Data JDBC repositories
5	Services	2	Обновить сервисы для repositories
6	Controllers & Exception Handling	2	Финализировать слои
7	Application	1	Main класс приложения
8	Tests	4	Юнит и интеграционные тесты
9	Database	1	SQL скрипты инициализации
10	Documentation	3	README и архитектура
Итого: 27 микрокоммитов для полной миграции

Архитектура проекта
Clean Architecture layers
text
┌─────────────────────────────────────────────────────────┐
│  REST Controllers (PostController)                      │
├─────────────────────────────────────────────────────────┤
│  DTOs (PostDetailDto, CommentDto, PostsResponse)        │
├─────────────────────────────────────────────────────────┤
│  Service Layer (PostService, CommentService)            │
├─────────────────────────────────────────────────────────┤
│  Repository Layer (Spring Data JDBC)                    │
├─────────────────────────────────────────────────────────┤
│  Model Layer (Post, Comment, PostTag - Java 21 records) │
├─────────────────────────────────────────────────────────┤
│  PostgreSQL Database                                    │
└─────────────────────────────────────────────────────────┘
Data flow
text
HTTP Request
↓
PostController (REST handler)
↓
PostService (Business logic)
↓
PostRepository (Spring Data JDBC)
↓
PostgreSQL Database
↓
Response (JSON DTOs)
Java 21 features в проекте
1. Record Types
   Records заменяют traditional classes с Lombok:

Для entities (models):

java
@Table("posts")
public record Post(
@Id Long id,
@Column("title") String title,
@Column("text") String text,
// ... другие поля
) {
// Compact constructor для валидации
public Post {
if (title == null || title.isBlank()) {
throw new IllegalArgumentException("Title cannot be blank");
}
}
}
Для DTOs:

java
public record PostDetailDto(
Long id,
String title,
String text,
List<String> tags,
@JsonProperty("likesCount") Integer likesCount,
@JsonProperty("commentsCount") Integer commentsCount
) {
// Compact constructor для валидации
public PostDetailDto {
if (title == null) throw new IllegalArgumentException("Title required");
}
}
Для exception responses:

java
public record ErrorResponse(
@JsonProperty("timestamp") long timestamp,
@JsonProperty("status") int status,
@JsonProperty("message") String message,
@JsonProperty("path") String path,
@JsonProperty("details") String details
) {
// Compact constructor
public ErrorResponse {
if (message == null || message.isBlank()) {
throw new IllegalArgumentException("Message required");
}
}
}
2. Text Blocks
   Многострочные SQL запросы без конкатенации:

java
@Query("""
SELECT * FROM posts
WHERE LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
OR LOWER(text) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
ORDER BY created_at DESC
""")
List<Post> searchPosts(@Param("searchTerm") String searchTerm);
3. Pattern Matching
   В exception handling (если понадобится):

java
if (exception instanceof EntityNotFoundException ex) {
log.warn("Entity not found: {}", ex.getMessage());
return ResponseEntity.notFound().build();
}
4. Enhanced Switch (future versions)
   Может использоваться для pattern matching в switch:

java
switch (post) {
case Post p when p.likesCount() > 100 -> logPopularPost(p);
case Post p -> logRegularPost(p);
}
5. Sealed Classes (future enhancement)
   Для более строгой типизации можно использовать:

java
public sealed interface Entity permits Post, Comment, PostTag {
Long id();
}
Структура кода
Каталоги проекта
text
blog-backend-java21/
│
├── build.gradle.kts                    # Gradle конфигурация с Java 21
├── settings.gradle.kts
├── gradle.properties
├── gradlew, gradlew.bat               # Gradle wrapper
│
├── docker-compose.yml                 # PostgreSQL для разработки
├── .gitignore                         # Git игнор правила
│
├── README.md                          # Этот файл
├── ARCHITECTURE.md                    # Документация архитектуры
├── JAVA21_MIGRATION_PLAN.txt         # Полный план миграции
│
├── src/main/
│   ├── java/ru/yandex/practicum/
│   │   ├── BlogApplication.java                  # Entry point
│   │   │
│   │   ├── model/                                # Entities (Java 21 records)
│   │   │   ├── Post.java
│   │   │   ├── Comment.java
│   │   │   └── PostTag.java
│   │   │
│   │   ├── repository/                          # Spring Data JDBC
│   │   │   ├── PostRepository.java
│   │   │   ├── CommentRepository.java
│   │   │   └── PostTagRepository.java
│   │   │
│   │   ├── service/                             # Business Logic
│   │   │   ├── PostService.java
│   │   │   ├── PostServiceImpl.java
│   │   │   ├── CommentService.java
│   │   │   └── CommentServiceImpl.java
│   │   │
│   │   ├── controller/                          # REST Controllers
│   │   │   └── PostController.java
│   │   │
│   │   ├── dto/                                 # DTOs (Java 21 records)
│   │   │   ├── PostDetailDto.java
│   │   │   ├── PostListItemDto.java
│   │   │   ├── CommentDto.java
│   │   │   └── PostsResponse.java
│   │   │
│   │   └── exception/                           # Exception Handling
│   │       ├── GlobalExceptionHandler.java
│   │       └── ErrorResponse.java (record)
│   │
│   └── resources/
│       ├── application.properties               # Production config
│       ├── schema.sql                           # PostgreSQL DDL
│       └── data.sql                             # Production test data
│
└── src/test/
├── java/ru/yandex/practicum/
│   ├── AbstractIntegrationTest.java         # Base for all tests
│   ├── service/
│   │   ├── PostServiceTest.java
│   │   └── CommentServiceTest.java
│   ├── controller/
│   │   └── PostControllerTest.java
│   └── BlogApplicationIntegrationTest.java  # E2E tests
│
└── resources/
├── application-test.properties          # Test config
└── data-test.sql                        # Test fixtures
API Endpoints
Posts Management
Метод	Endpoint	Описание	Статус
GET	/api/posts	Получить список постов (пагинация)	200
GET	/api/posts?search=term	Поиск постов	200
GET	/api/posts/{id}	Получить пост по ID	200/404
POST	/api/posts	Создать новый пост	201
PUT	/api/posts/{id}	Обновить пост	200/404
DELETE	/api/posts/{id}	Удалить пост	204/404
POST	/api/posts/{id}/likes	Увеличить лайки	200/404
PUT	/api/posts/{id}/image	Загрузить изображение	200/404
GET	/api/posts/{id}/image	Получить изображение	200/404
Comments Management
Метод	Endpoint	Описание	Статус
GET	/api/posts/{id}/comments	Получить комментарии	200
GET	/api/posts/{id}/comments/{cid}	Получить комментарий	200/404
POST	/api/posts/{id}/comments	Создать комментарий	201
PUT	/api/posts/{id}/comments/{cid}	Обновить комментарий	200/404
DELETE	/api/posts/{id}/comments/{cid}	Удалить комментарий	204/404
Query параметры
Для GET /api/posts:

pageNumber (int, default=1) - номер страницы

pageSize (int, default=10) - количество постов на странице

search (string, optional) - поисковый термин

Для POST /api/posts:

title (string, required) - заголовок поста

text (string, required) - основной текст

tags (List<String>, optional) - список тегов

Примеры запросов
bash
# 1. Получить все посты (первая страница)
curl http://localhost:8080/api/posts

# 2. Получить 5 постов со второй страницы
curl "http://localhost:8080/api/posts?pageNumber=2&pageSize=5"

# 3. Поиск постов по "Java"
curl "http://localhost:8080/api/posts?search=Java"

# 4. Создать новый пост
curl -X POST "http://localhost:8080/api/posts" \
-H "Content-Type: application/json" \
-d '{
"title": "Java 21 Features",
"text": "Record types, pattern matching...",
"tags": ["Java", "Spring", "News"]
}' \
-G -d "title=Java 21 Features" \
-d "text=Record types, pattern matching..." \
-d "tags=Java" -d "tags=Spring"

# 5. Получить пост по ID
curl http://localhost:8080/api/posts/1

# 6. Обновить пост
curl -X PUT "http://localhost:8080/api/posts/1" \
-G -d "title=Updated Title" \
-d "text=Updated content"

# 7. Удалить пост
curl -X DELETE http://localhost:8080/api/posts/1

# 8. Увеличить лайки
curl -X POST http://localhost:8080/api/posts/1/likes

# 9. Загрузить изображение
curl -X PUT "http://localhost:8080/api/posts/1/image" \
-F "image=@photo.jpg"

# 10. Получить все комментарии к посту
curl http://localhost:8080/api/posts/1/comments

# 11. Создать комментарий
curl -X POST "http://localhost:8080/api/posts/1/comments" \
-G -d "text=Great post!"

# 12. Удалить комментарий
curl -X DELETE http://localhost:8080/api/posts/1/comments/5
Сборка и запуск
Gradle команды
bash
# Показать информацию о проекте
./gradlew projects

# Показать все доступные задачи
./gradlew tasks

# Сборка проекта (компиляция + unit тесты)
./gradlew build

# Сборка без тестов
./gradlew build -x test

# Очистить build директорию
./gradlew clean

# Запуск приложения через Spring Boot
./gradlew bootRun

# Создать executable JAR
./gradlew bootJar

# Создать обычный JAR (без встроенного Tomcat)
./gradlew jar

# Проверить зависимости
./gradlew dependencies

# Обновить Gradle wrapper
./gradlew wrapper --gradle-version=latest
Запуск приложения
Через Gradle:

bash
./gradlew bootRun

# С дополнительными VM опциями
./gradlew bootRun --args='--server.port=9090'
Через JAR файл:

bash
# Сначала собрать JAR
./gradlew bootJar

# Потом запустить
java -jar build/libs/blog-application-java21.jar

# С системными свойствами
java -Dserver.port=9090 \
-Dspring.datasource.url=jdbc:postgresql://localhost:5432/blog_db \
-jar build/libs/blog-application-java21.jar
Через переменные окружения:

bash
# Linux/Mac
export SERVER_PORT=9090
export SPRING_DATASOURCE_PASSWORD=mypassword
./gradlew bootRun

# Windows (PowerShell)
$env:SERVER_PORT=9090
$env:SPRING_DATASOURCE_PASSWORD=mypassword
./gradlew bootRun

# Windows (CMD)
set SERVER_PORT=9090
set SPRING_DATASOURCE_PASSWORD=mypassword
gradlew.bat bootRun
Тестирование
Запуск тестов
bash
# Запустить все тесты
./gradlew test

# Запустить конкретный тестовый класс
./gradlew test --tests PostServiceTest

# Запустить конкретный тестовый метод
./gradlew test --tests PostServiceTest.testCreatePost_ShouldCreateNewPost

# Тесты с детальным выводом
./gradlew test --info

# Пропустить тесты при сборке
./gradlew build -x test
Типы тестов в проекте
1. Integration Tests (AbstractIntegrationTest):

Используют полный Spring контекст

Работают с реальной H2 БД

Наследуются от AbstractIntegrationTest для кеширования контекста

Файлы: *ServiceTest.java, BlogApplicationIntegrationTest.java

2. WebMvc Tests (PostControllerTest):

Используют @WebMvcTest для загрузки только веб-слоя

Мокируют сервисы через @MockBean

Используют MockMvc для тестирования HTTP

Файл: PostControllerTest.java

3. Test Context Caching:

Spring кеширует контекст между тестами с одинаковой конфигурацией

AbstractIntegrationTest обеспечивает переиспользование контекста

spring.test.context.cache.maxSize=10 в application-test.properties

Результат: тесты на 50-60% быстрее!

Coverage отчеты
bash
# Если добавить JaCoCo plugin в build.gradle:
./gradlew jacocoTestReport

# Отчет будет в: build/reports/jacoco/test/html/
Конфигурация
application.properties (Production)
text
# Server
server.port=8080
server.servlet.context-path=/api

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# SQL Initialization
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql

# Java 21 features
spring.threads.virtual.enabled=true

# Logging
logging.level.root=INFO
logging.level.ru.yandex.practicum=DEBUG
application-test.properties (Testing)
text
# H2 in-memory database
spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL
spring.datasource.driver-class-name=org.h2.Driver

# Test context caching
spring.test.context.cache.maxSize=10

# Logging
logging.level.root=INFO
logging.level.ru.yandex.practicum=DEBUG
Environment Variables
Можно переопределить любое свойство через переменные окружения:

bash
# Формат: SPRING_<SECTION>_<PROPERTY>
SPRING_DATASOURCE_URL=jdbc:postgresql://remote-host:5432/blog_db
SPRING_DATASOURCE_USERNAME=prod_user
SPRING_DATASOURCE_PASSWORD=secure_password
SERVER_PORT=8080
Troubleshooting
Ошибка: "Connection refused" к PostgreSQL
Симптомы:

text
org.postgresql.util.PSQLException: Connection to localhost:5432 refused.
Решение:

bash
# 1. Проверить что PostgreSQL запущен
docker-compose ps

# 2. Если контейнер не запущен, запустить его
docker-compose up -d

# 3. Если используется локальный PostgreSQL
pg_isready -h localhost -p 5432

# 4. Проверить параметры подключения в application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=postgres
spring.datasource.password=postgres
Ошибка: "Port 8080 already in use"
Решение 1: Использовать другой порт

bash
SERVER_PORT=9090 ./gradlew bootRun
Решение 2: Найти и убить процесс на порту 8080

bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows (PowerShell)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
Ошибка: "Database already exists"
Решение:

bash
# Дропить БД и пересоздать
docker-compose down -v
docker-compose up -d

# Или удалить БД вручную
psql -U postgres
DROP DATABASE IF EXISTS blog_db;
CREATE DATABASE blog_db;
Ошибка: "Java version 21 is not supported"
Решение:

bash
# Проверить версию Java
java -version

# Если Java < 21, установить Java 21 LTS
# https://www.oracle.com/java/technologies/downloads/#java21

# Переустановить переменные окружения JAVA_HOME
export JAVA_HOME=/path/to/java-21
Тесты падают с ошибкой БД
Причина: Контекст Spring не был правильно инициализирован

Решение:

bash
# Очистить кеш Gradle и пересобрать
./gradlew clean build

# Запустить тесты с более детальным выводом
./gradlew test --info --stacktrace
Slow tests
Причина: Контексты не кешируются или создаются заново

Решение:

Убедитесь что все тесты наследуются от AbstractIntegrationTest

Проверьте что spring.test.context.cache.maxSize=10 в application-test.properties

Минимизируйте использование @MockBean (каждый новый мок создает новый контекст)

Дополнительные ресурсы
Документация
Java 21 Documentation

Spring Boot 3.2 Reference

Spring Data JDBC Documentation

PostgreSQL Documentation

Gradle Documentation

JUnit 5 User Guide

Полезные файлы в проекте
JAVA21_MIGRATION_PLAN.txt - Полный план из 27 микрокоммитов

ARCHITECTURE.md - Документация архитектуры (если есть)

build.gradle.kts - Все зависимости и конфигурация

src/main/resources/schema.sql - Схема БД

src/main/resources/data.sql - Тестовые данные

Контрибьютинг
Если вы хотите улучшить проект:

Создайте branch для вашей фичи

Следуйте плану микрокоммитов из JAVA21_MIGRATION_PLAN.txt

Убедитесь что все тесты проходят: ./gradlew test

Создайте Pull Request с описанием изменений

