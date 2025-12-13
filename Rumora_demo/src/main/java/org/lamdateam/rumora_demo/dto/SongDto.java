package org.lamdateam.rumora_demo.dto;

import java.util.List;
import java.util.Objects;

public class SongDto {
    private Integer songId;
    private String songName;
    private String authorName;
    private Integer yearOfCreation;
    private String textSong;
    private String songCover;
    private List<CommentDto> comments;

    public SongDto() {}

    public Integer getSongId() { return songId; }
    public void setSongId(Integer songId) { this.songId = songId; }
    public String getSongName() { return songName; }
    public void setSongName(String songName) { this.songName = songName; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public Integer getYearOfCreation() { return yearOfCreation; }
    public void setYearOfCreation(Integer yearOfCreation) { this.yearOfCreation = yearOfCreation; }
    public String getTextSong() { return textSong; }
    public void setTextSong(String textSong) { this.textSong = textSong; }
    public String getSongCover() { return songCover; }
    public void setSongCover(String songCover) { this.songCover = songCover; }
    public List<CommentDto> getComments() { return comments; }
    public void setComments(List<CommentDto> comments) { this.comments = comments; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongDto)) return false;
        SongDto songDto = (SongDto) o;
        return Objects.equals(songId, songDto.songId) &&
                Objects.equals(songName, songDto.songName) &&
                Objects.equals(authorName, songDto.authorName) &&
                Objects.equals(yearOfCreation, songDto.yearOfCreation) &&
                Objects.equals(textSong, songDto.textSong) &&
                Objects.equals(songCover, songDto.songCover) &&
                Objects.equals(comments, songDto.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, songName, authorName, yearOfCreation, textSong, songCover, comments);
    }
}