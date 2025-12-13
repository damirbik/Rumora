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
public class CoverUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public CoverUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Только модератор и админ могут загружать обложки
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    @PostMapping("/cover")
    public ResponseEntity<FileUploadResponseDto> uploadCoverFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponseDto response = fileStorageService.storeCoverFile(file);
        return ResponseEntity.ok(response);
    }
}