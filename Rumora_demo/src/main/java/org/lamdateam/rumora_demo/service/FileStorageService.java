package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.FileUploadResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadBaseDir;

    private static final String UPLOAD_BASE_DIR = "uploads";
    private static final String AUDIO_DIR = UPLOAD_BASE_DIR + "/audio/";
    private static final String COVERS_DIR = UPLOAD_BASE_DIR + "/covers/";

    public FileUploadResponseDto storeAudioFile(MultipartFile file) {
        return storeFile(file, AUDIO_DIR);
    }

    public FileUploadResponseDto storeCoverFile(MultipartFile file) {
        return storeFile(file, COVERS_DIR);
    }

    private FileUploadResponseDto storeFile(MultipartFile file, String dir) {
        if (file.isEmpty()) {
            throw new RuntimeException("Нельзя загрузить пустой файл");
        }

        // Создаём папку при первой загрузке (а не при старте)
        Path dirPath = Paths.get(dir);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папку: " + dir, e);
        }

        // Генерация имени
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            Path filePath = dirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            String relativePath = dir + fileName;

            FileUploadResponseDto response = new FileUploadResponseDto();
            response.setFileName(fileName);
            response.setFileDownloadUri(relativePath);
            response.setSize(file.getSize());

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + fileName, e);
        }
    }
}