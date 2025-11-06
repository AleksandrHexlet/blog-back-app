package ru.yandex.practicum.initializer;

import ru.yandex.practicum.config.AppConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class BlogWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Создание Root Application Context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // Listener для инициализации контекста
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Создание Web Application Context для DispatcherServlet
        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(AppConfig.class);

        // Регистрация DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        // Конфигурация multipart
        registration.setMultipartConfig(
                new jakarta.servlet.MultipartConfigElement("",
                        5242880, // max file size (5MB)
                        20971520, // max request size (20MB)
                        1048576) // file size threshold (1MB)
        );
    }
}