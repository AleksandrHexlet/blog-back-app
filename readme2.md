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

