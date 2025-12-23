package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.service.SongManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/songs") // ← базовый путь
@CrossOrigin(origins = "http://localhost:3000, https://remjest-rumora-5a51.twc1.net")
public class SongManagementController {

    private final SongManagementService songManagementService;

    @Autowired
    public SongManagementController(SongManagementService songManagementService) {
        this.songManagementService = songManagementService;
    }

    // ✅ Обновление текстовых полей
    @PreAuthorize("hasAnyAuthority('Admin', 'Moder')")
    @PutMapping("/{songId}")
    public ResponseEntity<SongDto> updateSong(
            @PathVariable Integer songId,
            @RequestBody SongDto songDto
    ) {
        SongDto updated = songManagementService.updateSong(songId, songDto);
        return ResponseEntity.ok(updated);
    }

    // ✅ Удаление трека
    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable Integer songId) {
        songManagementService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Обновление обложки — ИСПРАВЛЕНО: убрано лишнее /songs
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/{songId}/cover")
    public ResponseEntity<SongDto> updateCover(
            @PathVariable Integer songId,
            @RequestParam MultipartFile songCover
    ) {
        SongDto updated = songManagementService.updateSongCover(songId, songCover);
        return ResponseEntity.ok(updated);
    }

    // ✅ Обновление аудио — ИСПРАВЛЕНО: убрано лишнее /songs
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/{songId}/audio")
    public ResponseEntity<SongDto> updateAudio(
            @PathVariable Integer songId,
            @RequestParam MultipartFile audioFile
    ) {
        SongDto updated = songManagementService.updateSongAudio(songId, audioFile);
        return ResponseEntity.ok(updated);
    }

    // ✅ Добавление трека

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping  // ← без /songs!
    public ResponseEntity<SongDto> addSong(
            @RequestParam String songName,
            @RequestParam String authorName,
            @RequestParam Integer yearOfCreation,
            @RequestParam(required = false) String textSong,
            @RequestParam(required = false) MultipartFile songCover,
            @RequestParam(required = false) MultipartFile audioFile
    ) {
        SongDto saved = songManagementService.addSong(
                songName, authorName, yearOfCreation, textSong, songCover, audioFile
        );
        return ResponseEntity.ok(saved);
    }
}