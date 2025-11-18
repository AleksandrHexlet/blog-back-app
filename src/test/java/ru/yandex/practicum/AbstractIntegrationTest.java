package ru.yandex.practicum;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * AbstractIntegrationTest это базовый класс для всех интеграционных тестов.
 *
 * Все интеграционные тесты должны наследоваться от этого класса.
 * Использование одной и той же конфигурации @SpringBootTest + @ActiveProfiles
 * гарантирует что Spring создаст контекст один раз и переиспользует его
 * для всех тестов.
 * Аннотации:
 * - @SpringBootTest: загружает полный контекст приложения
 * - @ActiveProfiles("test"): использует application-test.properties
 * - @TestPropertySource: дополнительные свойства для тестов
 * - @ExtendWith(SpringExtension.class): интеграция JUnit 5 с Spring
 *
 * @author Alex
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BlogApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.properties",
        properties = {
                "spring.test.context.cache.maxSize=10"
        }
)
public abstract class AbstractIntegrationTest {
    /**
     * Эта переменная может использоваться в подклассах.
     */
    protected static final String TEST_USER_NAME = "test_user";
    /**
     * Базовый URL для API тестов.
     */
    protected static final String API_BASE_PATH = "/posts";
}
