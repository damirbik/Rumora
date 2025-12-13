package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/songs")
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
}