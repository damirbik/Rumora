package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.service.SongManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Song> updateSong(@PathVariable Integer songId, @RequestBody Song song) {
        song.setSongId(songId);
        Song updated = songManagementService.updateSong(song);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable Integer songId) {
        songManagementService.deleteSong(songId);
        return ResponseEntity.ok().build();
    }
}