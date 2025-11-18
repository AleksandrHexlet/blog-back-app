-- ============================================
-- Blog Database Sample Data
-- ============================================

-- Clear existing data (if any)
DELETE FROM post_tags;
DELETE FROM comments;
DELETE FROM posts;

-- Reset auto-increment sequences
ALTER SEQUENCE posts_id_seq RESTART WITH 1;
ALTER SEQUENCE comments_id_seq RESTART WITH 1;

-- ============================================
-- Insert Sample Posts
-- ============================================

INSERT INTO posts (title, text, content, author, image, likes_count, likes, created_at, updated_at)
VALUES
(
    'Как начать изучение Java',
    'Изучение Java - это увлекательное путешествие в мир программирования.',
    'В этой статье мы рассмотрим основные шаги для начинающих разработчиков. Java - один из самых популярных языков программирования в мире. Он используется в разработке веб-приложений, мобильных приложений и систем обработки больших данных.',
    'Иван Петров',
    'https://example.com/java-tutorial.jpg',
    42,
    42,
    NOW() - INTERVAL '5 days',
    NOW() - INTERVAL '5 days'
),
(
    'Spring Boot для начинающих',
    'Spring Boot делает разработку Java приложений простой и быстрой.',
    'Spring Boot - это фреймворк, который упрощает разработку приложений на Java. Он предоставляет встроенный сервер приложений, упрощает конфигурацию и позволяет быстро создавать production-ready приложения. В этой статье мы рассмотрим основные возможности Spring Boot.',
    'Мария Сидорова',
    'https://example.com/spring-boot.jpg',
    38,
    38,
    NOW() - INTERVAL '3 days',
    NOW() - INTERVAL '3 days'
),
(
    'REST API Design Best Practices',
    'Проектирование хорошего REST API требует внимания к деталям.',
    'REST API является стандартом для разработки веб-сервисов. В этой статье мы обсудим best practices при проектировании REST API, включая правильное использование HTTP методов, правильные коды ответов и версионирование API.',
    'Сергей Иванов',
    'https://example.com/rest-api.jpg',
    56,
    56,
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '2 days'
),
(
    'PostgreSQL для веб-разработчиков',
    'PostgreSQL - мощная открытая база данных для хранения данных приложений.',
    'PostgreSQL предлагает надежность, производительность и расширяемость. Мы рассмотрим основные возможности, индексирование, оптимизацию запросов и лучшие практики работы с PostgreSQL в веб-приложениях.',
    'Александр Смирнов',
    'https://example.com/postgresql.jpg',
    29,
    29,
    NOW() - INTERVAL '7 days',
    NOW() - INTERVAL '7 days'
),
(
    'Тестирование Java кода с JUnit и Mockito',
    'Автоматизированное тестирование критически важно для качества кода.',
    'Учитесь писать эффективные тесты на Java с помощью JUnit и Mockito. Мы рассмотрим unit тесты, интеграционные тесты и как использовать mock объекты для изолированного тестирования компонентов.',
    'Елена Козлова',
    'https://example.com/testing.jpg',
    33,
    33,
    NOW() - INTERVAL '4 days',
    NOW() - INTERVAL '4 days'
);

-- ============================================
-- Insert Sample Comments
-- ============================================

INSERT INTO comments (post_id, text, author, created_at, updated_at)
VALUES
-- Comments for post 1 (Java)
(
    1,
    'Отличная статья! Очень полезна для начинающих. Помогла мне разобраться в основах.',
    'Петр Васильев',
    NOW() - INTERVAL '4 days',
    NOW() - INTERVAL '4 days'
),
(
    1,
    'Спасибо за подробное объяснение. Хотелось бы больше примеров кода.',
    'Анна Морозова',
    NOW() - INTERVAL '3 days',
    NOW() - INTERVAL '3 days'
),
(
    1,
    'Согласен, очень информативно. Жду продолжения серии статей!',
    'Владимир Лосев',
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '2 days'
),
-- Comments for post 2 (Spring Boot)
(
    2,
    'Spring Boot действительно упрощает разработку. Спасибо за четкое объяснение!',
    'Дмитрий Орлов',
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '2 days'
),
(
    2,
    'Можно больше примеров с конфигурацией? Это был самый сложный момент.',
    'Ольга Белова',
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
),
-- Comments for post 3 (REST API)
(
    3,
    'Очень полезная информация о REST API. Рекомендую всем разработчикам!',
    'Иван Кузнецов',
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
),
(
    3,
    'Для новичков немного сложновато, но разобрался. Спасибо!',
    'Натальи Степанова',
    NOW() - INTERVAL '6 hours',
    NOW() - INTERVAL '6 hours'
),
-- Comments for post 4 (PostgreSQL)
(
    4,
    'PostgreSQL - лучшая база данных! Согласен со всеми пунктами статьи.',
    'Борис Федоров',
    NOW() - INTERVAL '5 days',
    NOW() - INTERVAL '5 days'
),
(
    4,
    'Хотелось бы примеры оптимизации сложных запросов.',
    'Галина Никитина',
    NOW() - INTERVAL '3 days',
    NOW() - INTERVAL '3 days'
),
-- Comments for post 5 (Testing)
(
    5,
    'JUnit и Mockito - essential tools для любого Java разработчика!',
    'Максим Волков',
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '2 days'
),
(
    5,
    'Спасибо за практические примеры! Очень помогло.',
    'Сергей Павлов',
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
);

-- ============================================
-- Insert Sample Post Tags
-- ============================================

INSERT INTO post_tags (post_id, tag)
VALUES
-- Tags for post 1
(1, 'Java'),
(1, 'Программирование'),
(1, 'Обучение'),
(1, 'Beginners'),
-- Tags for post 2
(2, 'Java'),
(2, 'Spring'),
(2, 'Backend'),
(2, 'Framework'),
-- Tags for post 3
(3, 'REST'),
(3, 'API'),
(3, 'Design'),
(3, 'Best Practices'),
-- Tags for post 4
(4, 'Database'),
(4, 'PostgreSQL'),
(4, 'SQL'),
(4, 'Performance'),
-- Tags for post 5
(5, 'Testing'),
(5, 'Java'),
(5, 'JUnit'),
(5, 'Quality Assurance');

-- ============================================
-- Verify Data
-- ============================================

-- Count records
SELECT 'Posts:' as type, COUNT(*) as count FROM posts
UNION ALL
SELECT 'Comments:' as type, COUNT(*) as count FROM comments
UNION ALL
SELECT 'Tags:' as type, COUNT(*) as count FROM post_tags;