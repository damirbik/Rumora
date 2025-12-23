package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.service.SongManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/admin/songs")
@CrossOrigin(origins = "http://localhost:3000")

public class SongManagementController {

    private final SongManagementService songManagementService;

    @Autowired
    public SongManagementController(SongManagementService songManagementService) {
        this.songManagementService = songManagementService;
    }

    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    @PutMapping("/{songId}")
    public ResponseEntity<SongDto> updateSong(
            @PathVariable Integer songId,
            @RequestBody SongDto songDto  // ← Тип: SongDto, имя: songDto
    ) {
        SongDto updated = songManagementService.updateSong(songId, songDto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable Integer songId) {
        songManagementService.deleteSong(songId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/songs")
    public ResponseEntity<SongDto> addSong(
            @RequestParam String songName,
            @RequestParam String authorName,
            @RequestParam Integer yearOfCreation,
            @RequestParam(required = false) String textSong,
            @RequestParam MultipartFile songCover,   // обложка
            @RequestParam MultipartFile audioFile    // MP3
    ) {
        SongDto saved = songManagementService.addSong(
                songName, authorName, yearOfCreation, textSong, songCover, audioFile
        );
        return ResponseEntity.ok(saved);
    }
}