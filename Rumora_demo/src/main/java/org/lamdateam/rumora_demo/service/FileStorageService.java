package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.FileUploadResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String AUDIO_DIR = "uploads/audio/";
    private static final String COVERS_DIR = "uploads/covers/";

    static {
        try {
            Files.createDirectories(Paths.get(AUDIO_DIR));
            Files.createDirectories(Paths.get(COVERS_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папки для загрузки", e);
        }
    }

    public FileUploadResponseDto storeAudioFile(MultipartFile file) {
        return storeFile(file, AUDIO_DIR, "audio");
    }

    public FileUploadResponseDto storeCoverFile(MultipartFile file) {
        return storeFile(file, COVERS_DIR, "covers");
    }

    private FileUploadResponseDto storeFile(MultipartFile file, String dir, String type) {
        // Валидация
        if (file.isEmpty()) {
            throw new RuntimeException("Нельзя загрузить пустой файл");
        }

        // Генерация уникального имени
        String extension = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            // Сохранение
            Path filePath = Paths.get(dir).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Относительный путь для БД (как в твоей схеме!)
            String relativePath = type + "/" + fileName;

            FileUploadResponseDto response = new FileUploadResponseDto();
            response.setFileName(fileName);
            response.setFileDownloadUri("/uploads/" + relativePath);
            response.setSize(file.getSize());

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + fileName, e);
        }
    }
}