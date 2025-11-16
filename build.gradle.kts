plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.yandex.practicum"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_21

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// PostgreSQL Driver
	runtimeOnly("org.postgresql:postgresql")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Jackson (JSON processing) - included in spring-boot-starter-web

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2") // For in-memory tests
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Optional: Testcontainers for PostgreSQL integration tests
	// testImplementation("org.springframework.boot:spring-boot-testcontainers")
	// testImplementation("org.testcontainers:postgresql")
	// testImplementation("org.testcontainers:junit-jupiter")
}

tasks.withType<Test> {
	useJUnitPlatform()

	// Оптимизация кеширования контекста в тестах
	systemProperty("spring.test.context.cache.maxSize", "10")

	// Параллельное выполнение тестов (опционально)
	// maxParallelForks = Runtime.getRuntime().availableProcessors().div(2).takeIf { it > 0 } ?: 1

	// Логирование тестов
	testLogging {
		events("passed", "skipped", "failed")
		showStandardStreams = false
	}
}

// Конфигурация для создания Executable JAR
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	archiveFileName.set("blog-application.jar")
	launchScript()
}

// Отключить создание plain JAR (нужен только bootJar)
tasks.named<Jar>("jar") {
	enabled = false
}