package org.lamdateam.rumora_demo.dto;

import java.util.List;
import java.util.Objects;

public class SongSearchResultDto {
    private Integer songId;
    private String songName;
    private String authorName;
    private Integer yearOfCreation;
    private String songCover;
    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    private String audioFile;

    public List<CommentDto> getComments() {
        return comments;
    }

    private List<CommentDto> comments;

    public SongSearchResultDto() {}

    public Integer getSongId() { return songId; }
    public void setSongId(Integer songId) { this.songId = songId; }
    public String getSongName() { return songName; }
    public void setSongName(String songName) { this.songName = songName; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public Integer getYearOfCreation() { return yearOfCreation; }
    public void setYearOfCreation(Integer yearOfCreation) { this.yearOfCreation = yearOfCreation; }
    public String getSongCover() { return songCover; }
    public void setSongCover(String songCover) { this.songCover = songCover; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongSearchResultDto)) return false;
        SongSearchResultDto that = (SongSearchResultDto) o;
        return Objects.equals(songId, that.songId) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(authorName, that.authorName) &&
                Objects.equals(yearOfCreation, that.yearOfCreation) &&
                Objects.equals(audioFile, that.audioFile) &&
                Objects.equals(songCover, that.songCover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, songName, authorName, yearOfCreation, audioFile, songCover);
    }
}