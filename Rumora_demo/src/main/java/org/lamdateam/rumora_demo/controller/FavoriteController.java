package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.service.FavoriteSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/check/{songId}")
    public ResponseEntity<Boolean> isSongInFavorites(@PathVariable Integer songId, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        boolean isInFavorites = favoriteSongService.isSongInFavorites(userId, songId);
        return ResponseEntity.ok(isInFavorites);
    }

    // === НОВЫЙ МЕТОД ===
    @GetMapping
    public ResponseEntity<List<SongDto>> getFavoriteSongs(Authentication auth) {
        System.out.println("Auth object: " + auth); // ← Добавь
        if (auth == null || auth.getPrincipal() == null) {
            System.out.println("Auth is null or principal is null");
            return ResponseEntity.status(401).build();
        }
        System.out.println("Authenticated user: " + auth.getName()); // ← Добавь
        System.out.println("Authorities: " + auth.getAuthorities()); // ← Добавь

        Long userId = Long.parseLong(auth.getName());
        List<SongDto> favoriteSongs = favoriteSongService.getFavoriteSongsByUserId(userId);
        return ResponseEntity.ok(favoriteSongs);
    }
}