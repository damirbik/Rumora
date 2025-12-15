package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.service.FavoriteSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")

public class FavoriteController {

    private final FavoriteSongService favoriteSongService;

    @Autowired
    public FavoriteController(FavoriteSongService favoriteSongService) {
        this.favoriteSongService = favoriteSongService;
    }

    @PostMapping("/{songId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Integer songId, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        favoriteSongService.addToFavorites(userId, songId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Integer songId, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        favoriteSongService.removeFromFavorites(userId, songId);
        return ResponseEntity.ok().build();
    }
}