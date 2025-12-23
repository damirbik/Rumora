package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.dto.SongSearchResultDto;
import org.lamdateam.rumora_demo.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:3000, https://remjest-rumora-5a51.twc1.net")

public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Integer id) {
        SongDto songDto = songService.getSongById(id);
        return ResponseEntity.ok(songDto);
    }

    @GetMapping
    public ResponseEntity<List<SongSearchResultDto>> getAllSongs() {
        List<SongSearchResultDto> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
}