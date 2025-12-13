package org.lamdateam.rumora_demo.dto;

import java.util.Objects;

public class CommentDto {
    private String author;
    private String text;
    private Integer replyToCommentId;

    public CommentDto() {}

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Integer getReplyToCommentId() { return replyToCommentId; }
    public void setReplyToCommentId(Integer replyToCommentId) { this.replyToCommentId = replyToCommentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(text, that.text) &&
                Objects.equals(replyToCommentId, that.replyToCommentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, text, replyToCommentId);
    }
}