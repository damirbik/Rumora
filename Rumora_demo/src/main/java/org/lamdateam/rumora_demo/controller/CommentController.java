package org.lamdateam.rumora_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.security.JwtTokenProvider;
import org.lamdateam.rumora_demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000, https://remjest-rumora-5a51.twc1.net")

public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider; // ← Внедряем для извлечения userId

    @Autowired
    public CommentController(CommentService commentService, JwtTokenProvider jwtTokenProvider) {
        this.commentService = commentService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/{songId}")
    public ResponseEntity<Void> addComment(
            @PathVariable Integer songId,
            @RequestBody CommentRequest request,
            HttpServletRequest requestHttp
    ) {
        // Извлекаем userId из токена (как в FavoriteController)
        String authHeader = requestHttp.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String userIdStr = jwtTokenProvider.getUsernameFromToken(token); // sub = userId
        Long userId = Long.parseLong(userIdStr);

        // Передаём только текст комментария и ID трека — безопасно!
        commentService.addComment(songId, userId, request.getComment());
        return ResponseEntity.ok().build();
    }

    public static class CommentRequest {
        private String comment;

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}