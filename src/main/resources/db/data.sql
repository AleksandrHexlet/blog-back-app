-- Добавление тестовых данных
INSERT INTO posts (title, text, author_id, likes_count, image)
VALUES
  ('First Post', 'This is the first post', 1, 5, 'image1.jpg'),
  ('Second Post', 'This is the second post', 2, 10, 'image2.jpg'),
  ('Third Post', 'This is the third post', 1, 3, 'image3.jpg');

INSERT INTO comments (post_id, text, author_id)
VALUES
  (1, 'Great post!', 2),
  (1, 'Thanks for sharing', 3),
  (2, 'Very informative', 1);
