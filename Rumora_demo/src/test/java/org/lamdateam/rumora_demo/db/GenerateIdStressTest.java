package org.lamdateam.rumora_demo.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.HashSet;
import java.util.Set;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenerateIdStressTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("schema.sql");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void insertMany_users_haveUnique10DigitIds() {
        Integer roleId = jdbc.queryForObject(
                "SELECT role_id FROM roles WHERE role_name = 'пользователь'", Integer.class);

        // Вставляем N пользователей и проверяем уникальность user_id
        final int N = 200;
        Set<Long> ids = new HashSet<>(N);

        for (int i = 0; i < N; i++) {
            String u = "stress_u_" + i;
            String e = "stress_u_" + i + "@mail.test";
            Long id = jdbc.queryForObject(
                    "INSERT INTO users(username,email,password_hash,role_id) VALUES (?,?,?,?) RETURNING user_id",
                    Long.class, u, e, "password8", roleId);
            Assertions.assertNotNull(id);
            Assertions.assertTrue(id >= 1_000_000_000L && id <= 9_999_999_999L);
            Assertions.assertTrue(ids.add(id), "Коллизия user_id на вставке #" + i);
        }

        // Доп проверка на уровне базы
        Integer distinct = jdbc.queryForObject("SELECT COUNT(DISTINCT user_id) FROM users", Integer.class);
        Integer total = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        Assertions.assertEquals(total, distinct, "user_id должны быть уникальны");
    }
}

