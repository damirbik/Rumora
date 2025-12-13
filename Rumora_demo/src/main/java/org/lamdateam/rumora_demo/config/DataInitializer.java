package org.lamdateam.rumora_demo.config;

import org.lamdateam.rumora_demo.entity.Author;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.entity.UserRole;
import org.lamdateam.rumora_demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IRoleRepository userRoleRepository;
    private final IUserRepository userRepository;
    private final IAuthorRepository authorRepository;
    private final ISongRepository songRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(
            IRoleRepository userRoleRepository,
            IUserRepository userRepository,
            IAuthorRepository authorRepository,
            ISongRepository songRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.songRepository = songRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // === 1. Создаём роли, если их нет ===
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

        // === 2. Создаём пользователей, если их нет ===
        if (userRepository.count() == 0) {
            Optional<UserRole> adminOpt = userRoleRepository.findByRoleName("Admin");
            Optional<UserRole> moderOpt = userRoleRepository.findByRoleName("Moder");

            if (!adminOpt.isPresent() || !moderOpt.isPresent()) {
                throw new RuntimeException("Роли Admin или Moder не найдены");
            }

            UserRole adminRole = adminOpt.get();
            UserRole moderRole = moderOpt.get();

            // Админы
            User admin1 = new User("admin1",  passwordEncoder.encode("admin123"));
            admin1.setRole(adminRole);

            User superadmin = new User("superadmin",  passwordEncoder.encode("superpass"));
            superadmin.setRole(adminRole);

            // Модераторы
            User moder1 = new User("moder1", passwordEncoder.encode("moder123"));
            moder1.setRole(moderRole);

            User musicmod = new User("musicmod", passwordEncoder.encode("musicpass"));
            musicmod.setRole(moderRole);

            userRepository.saveAll(Arrays.asList(admin1, superadmin, moder1, musicmod));
            System.out.println("✅ Создано 2 админа и 2 модератора.");
        }

        // === 3. Создаём автора Channels4, если его нет ===
        Optional<Author> channels4Opt = authorRepository.findByAuthorName("Channels4");
        Author channels4;
        if (channels4Opt.isEmpty()) {
            channels4 = new Author();
            channels4.setAuthorName("Channels4");
            channels4 = authorRepository.save(channels4);
            System.out.println("✅ Создан автор: Channels4");
        } else {
            channels4 = channels4Opt.get();
        }

        // === 4. Создаём 17 треков, если их ещё нет ===
        if (songRepository.count() == 0) {
            String[] trackTitles = {
                    "No One Knows",
                    "Sick",
                    "Devil In Your Heart",
                    "Heads Up",
                    "Baseball Bat",
                    "Smoke In The Sky",
                    "Black & White",
                    "Crying For The Moon",
                    "Yo Ho",
                    "Captain Hook",
                    "Sand Castle (feat. Akkogorilla)",
                    "Bully",
                    "Fathers",
                    "Diamond",
                    "No Solution",
                    "Let It End",
                    "Lion's Den"
            };

            for (int i = 0; i < trackTitles.length; i++) {
                String title = trackTitles[i];
                String audioFileName = String.format("%02d. %s.mp3", i + 1, title);
                String coverFileName = "channels4_profile.jpg";

                Song song = new Song();
                song.setSongName(title);
                song.setAuthor(channels4);
                song.setYearOfCreation(2025);
                song.setTextSong(""); // можно оставить пустым
                song.setSongCover("covers/" + coverFileName);
                song.setAudioFile("audio/" + audioFileName);

                songRepository.save(song);
            }
            System.out.println("✅ Добавлено 17 треков от Channels4.");
        }
    }
}