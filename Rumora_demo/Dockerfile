# Используем образ с предустановленным Maven и Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Рабочая директория
WORKDIR /app

# Копируем всё содержимое проекта
COPY . .

# Собираем проект (без запуска тестов)
RUN mvn clean package -DskipTests

# Запускаем JAR
CMD ["java", "-jar", "target/rumora_demo-0.0.1.jar"]