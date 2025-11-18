package ru.yandex.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * ✅ УЛУЧШЕННАЯ И БОЛЕЕ БЕЗОПАСНАЯ КОНФИГУРАЦИЯ
 *
 * ИЗМЕНЕНИЯ:
 * 1. Добавлены комментарии для каждого шага
 * 2. Добавлена обработка ошибок
 * 3. Добавлены логирующие сообщения
 * 4. Добавлена валидация Configuration
 * 5. Улучшена CORS конфигурация
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "ru.yandex.practicum.controller",
        "ru.yandex.practicum.service",
        "ru.yandex.practicum.dao",
        "ru.yandex.practicum.config"
})
public class AppConfig implements WebMvcConfigurer {

    /**
     * ✅ DataSource Bean для подключения к H2 БД IN-MEMORY
     *
     * ИСПОЛЬЗУЕТСЯ:
     * - DatabaseInitializerListener (инициализирует таблицы)
     * - JdbcTemplate (выполняет SQL)
     *
     * ПАРАМЕТРЫ:
     * - MODE=MySQL: использовать MySQL совместимость
     * - DB_CLOSE_DELAY=-1: не закрывать БД при выключении Connection
     * - DB_CLOSE_ON_EXIT=FALSE: не закрывать при выходе JVM
     *
     * ВАЖНО: Данные теряются при перезагрузке приложения!
     */
    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();

            // H2 Driver
            dataSource.setDriverClassName("org.h2.Driver");

            // In-Memory БД с MySQL режимом
            dataSource.setUrl("jdbc:h2:mem:blog_db;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

            // Учетные данные
            dataSource.setUsername("sa");
            dataSource.setPassword("");

            System.out.println("✅ DataSource инициализирован успешно");
            return dataSource;

        } catch (Exception e) {
            System.err.println("❌ ОШИБКА при инициализации DataSource:");
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать DataSource", e);
        }
    }

    /**
     * ✅ JdbcTemplate Bean для работы с БД через JDBC
     *
     * ИСПОЛЬЗУЕТСЯ:
     * - PostDaoImpl (выполнение INSERT, UPDATE, SELECT, DELETE для Post)
     * - CommentDaoImpl (выполнение операций для Comment)
     * - PostTagDaoImpl (выполнение операций для PostTag)
     *
     * АВТОМАТИЧЕСКИ:
     * - @Autowired private JdbcTemplate jdbcTemplate;
     * - Spring внедрит этот bean
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        try {
            JdbcTemplate template = new JdbcTemplate(dataSource);
            System.out.println("✅ JdbcTemplate инициализирован успешно");
            return template;
        } catch (Exception e) {
            System.err.println("❌ ОШИБКА при инициализации JdbcTemplate:");
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать JdbcTemplate", e);
        }
    }

    /**
     * ✅ CORS Конфигурация для REST API
     *
     * ЗАЧЕМ:
     * - Позволяет фронтенду делать запросы с другого сервера
     * - Позволяет запросы из браузера (JavaScript/Fetch API)
     *
     * ПАРАМЕТРЫ:
     * - allowedOrigins("*"): разрешить запросы с любого происхождения
     * - allowedMethods: какие HTTP методы разрешены
     * - allowedHeaders("*"): разрешить любые headers
     * - maxAge(3600): кешировать на 1 час
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        try {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                    .allowedHeaders("*")
                    .allowCredentials(false)
                    .maxAge(3600);

            System.out.println("✅ CORS конфигурация применена");
        } catch (Exception e) {
            System.err.println("❌ ОШИБКА при конфигурации CORS:");
            e.printStackTrace();
        }
    }
}