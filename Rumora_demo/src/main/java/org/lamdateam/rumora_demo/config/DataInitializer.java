package org.lamdateam.rumora_demo.config;

import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.entity.UserRole;
import org.lamdateam.rumora_demo.repository.IRoleRepository;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IRoleRepository userRoleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(
            IRoleRepository userRoleRepository,
            IUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Создаём роли, если их нет
        if (userRoleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(1);
            userRole.setRoleName("User");

            UserRole moderRole = new UserRole();
            moderRole.setRoleId(2);
            moderRole.setRoleName("Moder");

            UserRole adminRole = new UserRole();
            adminRole.setRoleId(3);
            adminRole.setRoleName("Admin");

            userRoleRepository.saveAll(Arrays.asList(userRole, moderRole, adminRole));
        }

        // 2. Создаём пользователей, если их нет
        if (userRepository.count() == 0) {
            UserRole adminRole = userRoleRepository.findByRoleName("Admin")
                    .orElseThrow(() -> new RuntimeException("Роль Admin не найдена"));
            UserRole moderRole = userRoleRepository.findByRoleName("Moder")
                    .orElseThrow(() -> new RuntimeException("Роль Moder не найдена"));

            // Создаём пользователей БЕЗ указания userId — пусть БД сама его сгенерирует
            User admin1 = new User("admin1", passwordEncoder.encode("admin123"));
            admin1.setRole(adminRole);

            User superadmin = new User("superadmin", passwordEncoder.encode("superpass"));
            superadmin.setRole(adminRole);

            User moder1 = new User("moder1", passwordEncoder.encode("moder123"));
            moder1.setRole(moderRole);

            User musicmod = new User("musicmod", passwordEncoder.encode("musicpass"));
            musicmod.setRole(moderRole);

            userRepository.saveAll(Arrays.asList(admin1, superadmin, moder1, musicmod));
            System.out.println("✅ Создано 2 админа и 2 модератора.");
        }
    }
}