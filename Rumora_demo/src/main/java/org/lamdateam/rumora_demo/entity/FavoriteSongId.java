package org.lamdateam.rumora_demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteSongId implements Serializable {

    private Long userId;
    private Integer songId;

    public FavoriteSongId() {}

    public FavoriteSongId(Long userId, Integer songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteSongId)) return false;
        FavoriteSongId that = (FavoriteSongId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(songId, that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, songId);
    }
}