# Blog Backend Application

## Быстрый старт

### 1. Требования
- Java 21 (JDK)
- Maven 3.9+
- Apache Tomcat 10.1+

### 2. Сборка
git clone https://github.com/your-username/blog-back-app.git
cd blog-back-app
mvn clean package

### 3. Развертывание на Tomcat
cp target/blog-back-app.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh

### 4. Проверка
http://localhost:8080/api/posts

### 5. Запуск тестов
mvn test


## Структура проекта
- `src/main/java` - исходный код
- `src/main/resources` - конфигурация и SQL скрипты
- `src/test/java` - тесты
- `pom.xml` - конфигурация Maven

## REST API Endpoints

### Посты
- `GET /api/posts?search=&pageNumber=1&pageSize=5` - список постов
- `POST /api/posts` - создать пост
- `POST /api/posts/{id}` - получить пост
- `PUT /api/posts/{id}` - обновить пост
- `DELETE /api/posts/{id}` - удалить пост
- `POST /api/posts/{id}/likes` - увеличить лайки
- `PUT /api/posts/{id}/image` - загрузить изображение
- `GET /api/posts/{id}/image` - получить изображение

### Комментарии
- `GET /api/posts/{postId}/comments` - список комментариев
- `GET /api/posts/{postId}/comments/{commentId}` - получить комментарий
- `POST /api/posts/{postId}/comments` - создать комментарий
- `PUT /api/posts/{postId}/comments/{commentId}` - обновить комментарий
- `DELETE /api/posts/{postId}/comments/{commentId}` - удалить комментарий

## Примеры запросов Postman

### Создать пост
POST http://localhost:8080/api/posts
Content-Type: application/json

{
"title": "Мой пост",
"text": "Текст поста",
"tags": ["java", "spring"]
}


### Получить список постов
GET http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=5

### Добавить комментарий
POST http://localhost:8080/api/posts/1/comments
Content-Type: application/json

{
"text": "Отличный пост!",
"postId": 1
}

## Технологический стек
- Spring Framework 6.1.14
- Hibernate 6.4.4
- H2 Database (по умолчанию)
- PostgreSQL (для production)
- JUnit 5 и Mockito (тестирование)
- Maven (сборка)

## Контактная информация
Email: support@yandex-practicum.ru

Версия: 1.0.0
Это полный готовый код проекта! Теперь вы можете:

Создать структуру папок:

blog-back-app/
├── src/
│   ├── main/
│   │   ├── java/ru/yandex/practicum/
│   │   │   ├── config/ (поместить все config классы)
│   │   │   ├── model/ (Post.java, Comment.java)
│   │   │   ├── dao/ (все DAO классы)
│   │   │   ├── service/ (все Service классы)
│   │   │   └── controller/ (все контроллеры)
│   │   └── resources/ (application.properties, db/, logback.xml)
│   └── test/
│       ├── java/ru/yandex/practicum/ (все test классы)
│       └── resources/ (application-test.properties)
├── pom.xml
├── .gitignore
└── README.md
Собрать проект:

bash
mvn clean package
Развернуть на Tomcat:

bash
cp target/blog-back-app.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh