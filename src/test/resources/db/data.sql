-- Вставка тестовых постов
INSERT INTO posts (title, text, tags, likes_count) VALUES
('Spring Framework Tutorial', '# Spring Framework', 'spring,java,web', 10);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('REST API Best Practices', '## REST API Design', 'rest,api,backend', 15);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('Clean Code Principles', '### Writing Clean Code', 'java,cleancode,bestpractices', 8);

INSERT INTO posts (title, text, tags, likes_count) VALUES
('Database Optimization', '## Performance Tuning', 'database,sql,performance', 5);

-- Вставка тестовых комментариев
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
