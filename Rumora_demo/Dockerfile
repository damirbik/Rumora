# Этап 1: Сборка приложения
FROM maven:3.9-eclipse-temurin-21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только то, что нужно для сборки (с кэшированием)
COPY pom.xml .
COPY src ./src

# Собираем JAR без тестов и DevTools
RUN mvn clean package -DskipTests -Dspring-boot.build-image.skip=true

# Этап 2: Финальный образ
FROM eclipse-temurin:21-jre-alpine

# Безопасность: создаём непривилегированного пользователя
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Рабочая папка
WORKDIR /app

# Копируем JAR из этапа сборки (любое имя)
COPY --from=build /app/target/*.jar app.jar
COPY uploads ./uploads

# Меняем владельца
RUN chown appuser:appgroup app.jar

# Переключаемся на безопасного пользователя
USER appuser

# Порт приложения
EXPOSE 8080

# Точка входа
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]