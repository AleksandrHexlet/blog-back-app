package ru.yandex.practicum;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.yandex.practicum")
public class WebConfiguration implements WebMvcConfigurer {
    // Реализация интерфейса WebMvcConfigurer для правильной инициализации Spring MVC
}