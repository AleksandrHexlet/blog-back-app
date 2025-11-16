-- ============================================
-- Minimal Test Data for Unit Tests
-- ============================================

-- Insert minimal test posts
INSERT INTO posts (id, title, text, author_id, likes_count) VALUES
(1, 'Test Post 1', 'This is test post 1', 1, 5),
(2, 'Test Post 2', 'This is test post 2', 2, 10);

-- Insert minimal test comments
INSERT INTO comments (id, post_id, text, author_id) VALUES
(1, 1, 'Test comment 1', 2),
(2, 1, 'Test comment 2', 3);

-- Insert minimal test tags
INSERT INTO post_tags (id, post_id, tag) VALUES
(1, 1, 'Java'),
(2, 1, 'Spring');