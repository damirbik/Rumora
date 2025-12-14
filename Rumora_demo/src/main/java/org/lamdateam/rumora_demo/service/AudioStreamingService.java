package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AudioStreamingService {

    private final ISongRepository songRepository;

    @Value("${app.uploads.dir:/app/uploads}")
    private String uploadsDir;

    @Autowired
    public AudioStreamingService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Resource loadAudioFile(Integer songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        Path filePath = Paths.get(uploadsDir).resolve(song.getAudioFile()).normalize();

        try {
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                return new UrlResource(filePath.toUri());
            } else {
                throw new RuntimeException("Аудиофайл не найден: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный путь к аудиофайлу", e);
        }
    }
}