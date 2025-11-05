WAR проект для Tomcat 10 с Spring MVC Структура проекта text blog-back-app/ ├── src/ │ ├── main/ │ │ ├── java/ │ │ │ └── ru/yandex/practicum/ │ │ │ ├── ru.yandex.practicum.configuration/ │ │ │ │ └── WebConfiguration.java │ │ │ └── controller/ │ │ │ └── HomeController.java │ │ └── webapp/ │ │ └── WEB-INF/ │ │ └── web.xml │ └── test/ ├── pom.xml └── README.md Инструкции по сборке и развёртыванию

Создание структуры проекта bash mkdir -p blog-back-app/src/main/java/ru/yandex/practicum/{ru.yandex.practicum.configuration,controller} mkdir -p blog-back-app/src/main/webapp/WEB-INF mkdir -p blog-back-app/src/test cd blog-back-app
Копирование файлов pom.xml — скопировать в корень проекта
src/main/webapp/WEB-INF/web.xml — DispatcherServlet конфигурация

src/main/java/ru/yandex/practicum/ru.yandex.practicum.configuration/WebConfiguration.java — Spring конфигурация

src/main/java/ru/yandex/practicum/controller/HomeController.java — REST контроллер

Сборка проекта bash cd blog-back-app mvn clean package После успешной сборки WAR файл будет находиться в:
text blog-back-app/target/blog-back-up.war 4. Развёртывание на Tomcat 10 Вариант 1: Копирование через файловую систему bash

Скопируйте WAR файл в папку webapps Tomcat
cp target/blog-back-up.war /path/to/tomcat/webapps/

Перезагрузите Tomcat (или перезапустите сервис)
$CATALINA_HOME/bin/shutdown.sh $CATALINA_HOME/bin/startup.sh Вариант 2: Использование Tomcat Manager UI Откройте браузер: http://localhost:8080/manager/html

Введите credentials (если требуется)

В разделе "Deploy" выберите WAR файл

Нажмите "Deploy"

Вариант 3: Использование curl bash curl -u admin:admin -F "file=@target/blog-back-up.war"
http://localhost:8080/manager/text/deploy?path=/blog-back-up 5. Проверка развёртывания Откройте браузер и обращайтесь к эндпоинтам:

http://localhost:8080/blog-back-up/home → "Welcome to Home"

http://localhost:8080/blog-back-up/hello → "Hello World!"

Проверка логов Логи находятся в: $CATALINA_HOME/logs/catalina.out
Должны видеть строки вроде:

text Mapped "{GET /blog-back-up/home}" onto public java.lang.String... Mapped "{GET /blog-back-up/hello}" onto public java.lang.String... Требования Java: 21 или выше

Maven: 3.6.0 или выше

Tomcat: 10.1.x (использует Jakarta EE 9+)

Spring: 6.1.0

Основные технологии Spring Framework 6.1.0 — framework для веб-приложений

Spring MVC — для обработки HTTP запросов

Jakarta Servlet API 6.0.0 — для совместимости с Tomcat 10

SLF4J + Logback — логирование

Удаления приложения с Tomcat Через Manager UI: нажмите "Undeploy" напротив приложения

Через файловую систему: удалите файл webapps/blog-back-up.war

Через curl:

bash curl -u admin:admin
http://localhost:8080/manager/text/undeploy?path=/blog-back-up Troubleshooting

HTTP 404 — No endpoint found Решение: Убедитесь, что:
Контроллер имеет аннотацию @RestController или @Controller

Методы имеют @GetMapping, @PostMapping и т.д.

Путь соответствует указанному в аннотациях

ClassNotFoundException: jakarta.servlet Решение: В pom.xml должна быть зависимость jakarta.servlet-api версии 6.0.0+

Приложение не развёртывается Решение: Проверьте логи Tomcat:

bash tail -f $CATALINA_HOME/logs/catalina.out Дополнительные команды Maven bash

Очистить build директорию
mvn clean

Построить проект
mvn build

Построить и пропустить тесты
mvn clean package -DskipTests

Вывести информацию о проекте
mvn help:describe
