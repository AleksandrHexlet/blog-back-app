-- Вставка тестовых постов (ВАЖНО: не указываем id, он автоинкрементируется)
INSERT INTO posts (title, text, tags, likes_count) VALUES
('Spring Framework Tutorial', '# Spring Framework\n\nLearn how to build web applications with Spring Framework 6.1.', 'spring,java,web', 10);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('REST API Best Practices', '## REST API Design\n\n1. Use proper HTTP methods\n2. Return correct status codes\n3. Version your API', 'rest,api,backend', 15);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('Clean Code Principles', '### Writing Clean Code\n\nClean code is important for maintainability and readability.', 'java,cleancode,bestpractices', 8);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('Database Optimization', '## Performance Tuning\n\nOptimizing database queries improves application performance.', 'database,sql,performance', 5);

-- Вставка тестовых комментариев (post_id должен быть правильным)
INSERT INTO comments (text, post_id) VALUES
('Great article! Very helpful.', 1);

INSERT INTO comments (text, post_id) VALUES
('I learned a lot from this post.', 1);

INSERT INTO comments (text, post_id) VALUES
('Thanks for the detailed explanation.', 2);

INSERT INTO comments (text, post_id) VALUES
('This is exactly what I was looking for!', 2);

INSERT INTO comments (text, post_id) VALUES
('Excellent tips on performance optimization.', 4);
