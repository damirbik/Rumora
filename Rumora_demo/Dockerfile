# Этап 1: Сборка
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Копируем только то, что нужно для сборки
COPY pom.xml .
COPY src ./src

# Собираем JAR
RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM eclipse-temurin:21-jre-alpine

# Создаём непривилегированного пользователя
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Рабочая папка
WORKDIR /app

# Копируем JAR
COPY --from=build /app/target/*.jar app.jar

# Копируем демо-файлы (если есть)
COPY uploads /app/uploads

# Меняем владельца
RUN chown -R appuser:appgroup /app

# Переключаемся на безопасного пользователя
USER appuser

# Порт
EXPOSE 8080

# Запуск
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]