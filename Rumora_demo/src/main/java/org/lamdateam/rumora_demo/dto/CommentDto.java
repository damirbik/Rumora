package org.lamdateam.rumora_demo.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentDto {
    private Integer commentId;
    private String authorName;
    private String text;
    private LocalDateTime createdAt;
    private Integer replyToCommentId;

    public CommentDto() {}

    public Integer getCommentId() { return commentId; }
    public void setCommentId(Integer commentId) { this.commentId = commentId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; } // ← ИСПРАВЛЕНО

    public Integer getReplyToCommentId() { return replyToCommentId; }
    public void setReplyToCommentId(Integer replyToCommentId) { this.replyToCommentId = replyToCommentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(commentId, that.commentId) &&
                Objects.equals(authorName, that.authorName) &&
                Objects.equals(text, that.text) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(replyToCommentId, that.replyToCommentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, authorName, text, createdAt, replyToCommentId);
    }
}