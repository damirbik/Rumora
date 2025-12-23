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
    private String uploadBaseDir; // например: "uploads"

    public FileUploadResponseDto storeAudioFile(MultipartFile file) {
        return storeFile(file, "audio");
    }

    public FileUploadResponseDto storeCoverFile(MultipartFile file) {
        return storeFile(file, "covers");
    }

    private FileUploadResponseDto storeFile(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new RuntimeException("Нельзя загрузить пустой файл");
        }

        String fullDir = uploadBaseDir + "/" + subDir;
        Path dirPath = Paths.get(fullDir);

        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию: " + fullDir, e);
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        Path filePath = dirPath.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + fileName, e);
        }

        // ✅ ВАЖНО: возвращаем путь БЕЗ "uploads/"
        String relativePath = subDir + "/" + fileName;

        FileUploadResponseDto response = new FileUploadResponseDto();
        response.setFileName(fileName);
        response.setFileDownloadUri(relativePath); // ← "covers/xxx.jpg"
        response.setSize(file.getSize());

        return response;
    }
}