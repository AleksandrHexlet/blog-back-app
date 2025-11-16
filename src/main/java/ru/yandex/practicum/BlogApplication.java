package ru.yandex.practicum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BlogApplication это главный класс приложения Blog Backend.
 *
 * Использует @SpringBootApplication аннотацию которая объединяет:
 * - @Configuration: определяет класс как источник bean конфигурации
 * - @EnableAutoConfiguration: включает автоматическую конфигурацию Spring Boot
 * - @ComponentScan: сканирует компоненты в пакете ru.yandex.practicum и подпакетах
 *
 * Приложение использует:
 * - Java 21 как язык программирования с record types
 * - Spring Boot 3.2+ фреймворк
 * - PostgreSQL как основную БД
 * - Spring Data JDBC для доступа к данным
 * - Gradle как систему сборки
 *
 * Для запуска приложения:
 * 1. Убедитесь что PostgreSQL запущен на localhost:5432
 * 2. Выполните: ./gradlew bootRun
 * 3. Приложение будет доступно на http://localhost:8080/api
 *
 * Для запуска тестов:
 * ./gradlew test
 *
 * Для создания JAR:
 * ./gradlew bootJar
 * Результат: build/libs/blog-application-java21.jar
 *
 * @since 1.0.0
 * @author Alex
 */
@SpringBootApplication
public class BlogApplication {
	/**
	 * Главный метод приложения.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
