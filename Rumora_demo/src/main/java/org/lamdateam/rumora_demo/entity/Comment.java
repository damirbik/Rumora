package org.lamdateam.rumora_demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    // ✅ Простое поле song_id (Integer), НЕ сущность!
    @Column(name = "song_id", nullable = false)
    private Integer songId;

    // ✅ Простое поле user_id (Long), НЕ сущность!
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Опционально: ссылка на родительский комментарий (для ответов)
    @Column(name = "related_comment_id")
    private Integer relatedCommentId;

    // Конструкторы
    public Comment() {}

    // Геттеры и сеттеры
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRelatedCommentId() {
        return relatedCommentId;
    }

    public void setRelatedCommentId(Integer relatedCommentId) {
        this.relatedCommentId = relatedCommentId;
    }
}