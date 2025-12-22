// org.lamdateam.rumora_demo.controller.FavoriteController.java

package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.service.FavoriteSongService;
import org.lamdateam.rumora_demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteSongService favoriteSongService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FavoriteController(
            FavoriteSongService favoriteSongService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.favoriteSongService = favoriteSongService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/{songId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Integer songId, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        favoriteSongService.addToFavorites(userId, songId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Integer songId, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        favoriteSongService.removeFromFavorites(userId, songId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{songId}")
    public ResponseEntity<Boolean> isSongInFavorites(@PathVariable Integer songId, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        boolean isInFavorites = favoriteSongService.isSongInFavorites(userId, songId);
        return ResponseEntity.ok(isInFavorites);
    }

    @GetMapping
    public ResponseEntity<List<SongDto>> getFavoriteSongs(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        List<SongDto> favoriteSongs = favoriteSongService.getFavoriteSongsByUserId(userId);
        return ResponseEntity.ok(favoriteSongs);
    }

    // Вспомогательный метод: извлекает userId из токена
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String userIdStr = jwtTokenProvider.getUsernameFromToken(token); // sub = userId
        return Long.parseLong(userIdStr);
    }
}