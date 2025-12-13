package org.lamdateam.rumora_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_songs")
@IdClass(org.lamdateam.rumora_demo.entity.FavoriteSongId.class)
public class FavoriteSong {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "song_id")
    private Integer songId;

    // Также можно оставить связи, если они нужны для навигации (опционально)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", insertable = false, updatable = false)
    private Song song;

    public FavoriteSong() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}