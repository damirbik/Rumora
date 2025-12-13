package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody Comment comment, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        comment.getUser().setUserId(userId);
        commentService.addComment(comment);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}