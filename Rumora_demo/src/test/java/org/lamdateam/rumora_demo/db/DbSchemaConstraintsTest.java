package org.lamdateam.rumora_demo.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import java.util.Objects;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DbSchemaConstraintsTest {

    // Поднимаем изолированный PostgreSQL для тестов
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("schema.sql");

    @Autowired
    JdbcTemplate jdbc;

    private Integer roleId(String roleName) {
        return jdbc.queryForObject(
                "SELECT role_id FROM roles WHERE role_name = ?", Integer.class, roleName);
    }

    @Test
    void seedRoles_existAndUnique() {
        // Проверяем, что три базовые роли загружены
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM roles", Integer.class);
        Assertions.assertEquals(3, count);

        // Повторная вставка существующего имени роли должна падать по UNIQUE
        Integer adminId = Objects.requireNonNull(roleId("администратор"));
        Assertions.assertTrue(adminId > 0);
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("INSERT INTO roles(role_name) VALUES (?)", "администратор"));
    }

    @Test
    void userInsert_success_generates10DigitId() {
        Integer userRole = Objects.requireNonNull(roleId("пользователь"));

        // Вставляем пользователя без явного user_id, функция генерит 10-значный id
        Long id = jdbc.queryForObject(
                "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                Long.class, "u1", "u1@mail.test", "password8", userRole);

        Assertions.assertNotNull(id);
        Assertions.assertTrue(id >= 1_000_000_000L && id <= 9_999_999_999L,
                "user_id должен быть 10-значным");
    }

    @Test
    void passwordTooShort_violatesCheck() {
        Integer modRole = Objects.requireNonNull(roleId("модератор"));

        // Проверяем CHECK (минимум 8 символов)
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.queryForObject(
                        "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                        Long.class, "u2", "u2@mail.test", "short", modRole)
        );
    }

    @Test
    void usernameAndEmail_uniqueConstraints() {
        Integer userRole = Objects.requireNonNull(roleId("пользователь"));

        // Первая вставка проходит
        Long id1 = jdbc.queryForObject(
                "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                Long.class, "u3", "u3@mail.test", "password8", userRole);
        Assertions.assertNotNull(id1);

        // Дублируем username — должно упасть по UNIQUE
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.queryForObject(
                        "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                        Long.class, "u3", "u3b@mail.test", "password8", userRole)
        );

        // Дублируем email — должно упасть по UNIQUE
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.queryForObject(
                        "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                        Long.class, "u3b", "u3@mail.test", "password8", userRole)
        );
    }

    @Test
    void foreignKeyRole_mustExist() {
        // Несуществующая роль -> нарушение внешнего ключа
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.queryForObject(
                        "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                        Long.class, "u4", "u4@mail.test", "password8", 999_999)
        );
    }
}

