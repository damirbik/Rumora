package org.lamdateam.rumora_demo.dto;

import java.util.Objects;

/**
 * DTO для передачи информации об авторе (исполнителе).
 */
public class AuthorDto {

    private Integer authorId;
    private String authorName;

    public AuthorDto() {}

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDto)) return false;
        AuthorDto that = (AuthorDto) o;
        return Objects.equals(authorId, that.authorId) &&
                Objects.equals(authorName, that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, authorName);
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}