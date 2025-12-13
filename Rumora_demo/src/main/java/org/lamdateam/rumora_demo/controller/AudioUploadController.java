package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.FileUploadResponseDto;
import org.lamdateam.rumora_demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class AudioUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public AudioUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Только модератор и админ могут загружать аудио
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    @PostMapping("/audio")
    public ResponseEntity<FileUploadResponseDto> uploadAudioFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponseDto response = fileStorageService.storeAudioFile(file);
        return ResponseEntity.ok(response);
    }
}