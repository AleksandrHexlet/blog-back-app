-- Тестовые данные для интеграционных тестов

DELETE FROM comments;
DELETE FROM post_tags;
DELETE FROM posts;

INSERT INTO posts (id, title, text, author_id, likes_count, created_at, updated_at) VALUES
(1, 'Title 1', 'Content 1', NULL, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Title 2', 'Content 2', NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO post_tags (post_id, tag) VALUES
(1, 'tag1'),
(1, 'tag2'),
(2, 'tag3');

INSERT INTO comments (post_id, text, created_at, updated_at) VALUES
(1, 'Comment 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Comment 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);