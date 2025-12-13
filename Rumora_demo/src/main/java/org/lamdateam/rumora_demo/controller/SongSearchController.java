package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongSearchResultDto;
import org.lamdateam.rumora_demo.service.SongSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SongSearchController {

    private final SongSearchService songSearchService;

    @Autowired
    public SongSearchController(SongSearchService songSearchService) {
        this.songSearchService = songSearchService;
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<SongSearchResultDto>> searchByTitle(@RequestParam String q) {
        return ResponseEntity.ok(songSearchService.searchByTitle(q));
    }

    @GetMapping("/by-artist")
    public ResponseEntity<List<SongSearchResultDto>> searchByArtist(@RequestParam String q) {
        return ResponseEntity.ok(songSearchService.searchByArtist(q));
    }
}