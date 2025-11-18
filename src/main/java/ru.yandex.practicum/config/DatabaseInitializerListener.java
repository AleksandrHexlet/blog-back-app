package ru.yandex.practicum.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * ✅ Jakarta Servlet API (Java 21 compatible)
 * БЕЗ Spring зависимостей
 * СОЗДАЕТ ВСЕ ТАБЛИЦЫ включая POST_TAGS
 */
@WebListener
public class DatabaseInitializerListener implements ServletContextListener {

    private static final String URL = "jdbc:h2:mem:blog_db;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("🔥 DATABASE INITIALIZATION STARTED");
        System.out.println("════════════════════════════════════════════════════════════\n");

        try {
            System.out.println("[1/5] Loading H2 Driver...");
            Class.forName("org.h2.Driver");
            System.out.println("  ✅ Driver loaded\n");

            System.out.println("[2/5] Creating connection...");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("  ✅ Connection established\n");

                try (Statement stmt = conn.createStatement()) {
                    System.out.println("[3/5] Creating tables...");

                    // Таблица POSTS
                    stmt.execute(
                            "CREATE TABLE IF NOT EXISTS posts (" +
                                    "  id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                                    "  title VARCHAR(255) NOT NULL," +
                                    "  text LONGTEXT NOT NULL," +
                                    "  author_id BIGINT," +
                                    "  likes_count INT DEFAULT 0," +
                                    "  image LONGBLOB" +
                                    ")"
                    );
                    System.out.println("  ✅ POSTS table created");

                    // Таблица COMMENTS
                    stmt.execute(
                            "CREATE TABLE IF NOT EXISTS comments (" +
                                    "  id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                                    "  post_id BIGINT NOT NULL," +
                                    "  text LONGTEXT NOT NULL," +
                                    "  author_id BIGINT," +
                                    "  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE" +
                                    ")"
                    );
                    System.out.println("  ✅ COMMENTS table created");

                    // ✅ Таблица POST_TAGS
                    stmt.execute(
                            "CREATE TABLE IF NOT EXISTS post_tags (" +
                                    "  id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                                    "  post_id BIGINT NOT NULL," +
                                    "  tag VARCHAR(255) NOT NULL," +
                                    "  FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE" +
                                    ")"
                    );
                    System.out.println("  ✅ POST_TAGS table created\n");

                    System.out.println("[4/5] Inserting test data...");

                    // Вставить посты
                    stmt.execute(
                            "INSERT INTO posts (title, text, author_id, likes_count) VALUES " +
                                    "('First Post', 'This is the first post', 1, 5)," +
                                    "('Second Post', 'This is the second post', 2, 10)," +
                                    "('Third Post', 'This is the third post', 1, 3)"
                    );
                    System.out.println("  ✅ 3 posts inserted");

                    // Вставить комментарии
                    stmt.execute(
                            "INSERT INTO comments (post_id, text, author_id) VALUES " +
                                    "(1, 'Great post!', 2)," +
                                    "(1, 'Thanks for sharing!', 3)"
                    );
                    System.out.println("  ✅ 2 comments inserted");

                    // ✅ Вставить тэги
                    stmt.execute(
                            "INSERT INTO post_tags (post_id, tag) VALUES " +
                                    "(1, 'Java')," +
                                    "(1, 'Spring')," +
                                    "(2, 'Database')," +
                                    "(3, 'REST')"
                    );
                    System.out.println("  ✅ 4 tags inserted\n");

                    System.out.println("════════════════════════════════════════════════════════════");
                    System.out.println("✅✅✅ DATABASE SUCCESSFULLY INITIALIZED! ✅✅✅");
                    System.out.println("════════════════════════════════════════════════════════════\n");

                } catch (Exception e) {
                    System.err.println("❌ INITIALIZATION FAILED!");
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Database initialization failed", e);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ DATABASE CONNECTION FAILED!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("👋 Application shutting down");
    }
}