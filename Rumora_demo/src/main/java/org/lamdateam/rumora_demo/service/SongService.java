package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.CommentDto;
import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.dto.SongSearchResultDto;
import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService {

    private final ISongRepository songRepository;

    @Autowired
    public SongService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public SongDto getSongById(Integer songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : song.getComments()) {
            CommentDto dto = new CommentDto();
            dto.setAuthor(comment.getUser().getUsername());
            dto.setText(comment.getCommentText());
            if (comment.getRelatedComment() != null) {
                dto.setReplyToCommentId(comment.getRelatedComment().getCommentId());
            }
            commentDtos.add(dto);
        }

        SongDto songDto = new SongDto();
        songDto.setSongId(song.getSongId());
        songDto.setSongName(song.getSongName());
        songDto.setAuthorName(song.getAuthor().getAuthorName());
        songDto.setYearOfCreation(song.getYearOfCreation());
        songDto.setTextSong(song.getTextSong());
        songDto.setSongCover(song.getSongCover());
        songDto.setAudioFile(song.getAudioFile());
        songDto.setComments(commentDtos);

        return songDto;
    }

    public List<SongSearchResultDto> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        return mapToSearchResultDto(songs);
    }

    // Вспомогательный метод (можно вынести в отдельный маппер позже)
    private List<SongSearchResultDto> mapToSearchResultDto(List<Song> songs) {
        List<SongSearchResultDto> dtos = new ArrayList<>();
        for (Song song : songs) {
            SongSearchResultDto dto = new SongSearchResultDto();
            dto.setSongId(song.getSongId());
            dto.setSongName(song.getSongName());
            dto.setAuthorName(song.getAuthor().getAuthorName());
            dto.setYearOfCreation(song.getYearOfCreation());
            dto.setSongCover(song.getSongCover());
            dto.setAudioFile(song.getAudioFile());
            //dto.setComments(song.getComments());
            dtos.add(dto);
        }
        return dtos;
    }
}