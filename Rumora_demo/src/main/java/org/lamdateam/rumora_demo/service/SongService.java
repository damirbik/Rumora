package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.CommentDto;
import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.dto.SongSearchResultDto;
import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.entity.User;
import org.lamdateam.rumora_demo.repository.ICommentRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {

    private final ISongRepository songRepository;
    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;

    @Autowired
    public SongService(
            ISongRepository songRepository,
            ICommentRepository commentRepository,
            IUserRepository userRepository
    ) {
        this.songRepository = songRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public SongDto getSongById(Integer songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        // Получаем комментарии к треку по songId
        List<Comment> comments = commentRepository.findBySongId(songId);

        // Преобразуем комментарии в DTO
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto dto = new CommentDto();

            // 🔑 ОБЯЗАТЕЛЬНО: устанавливаем ID комментария!
            dto.setCommentId(comment.getCommentId());

            // Получаем имя автора по userId
            String authorName = userRepository.findById(comment.getUserId())
                    .map(User::getUsername)
                    .orElse("Unknown");
            dto.setAuthorName(authorName);

            dto.setText(comment.getCommentText());
            dto.setCreatedAt(comment.getCreatedAt());

            if (comment.getRelatedCommentId() != null) {
                dto.setReplyToCommentId(comment.getRelatedCommentId());
            }

            return dto;
        }).collect(Collectors.toList());

        // Формируем DTO трека
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

    // Вспомогательный метод для преобразования списка треков в DTO
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
            dtos.add(dto);
        }
        return dtos;
    }
}