package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.entity.Author;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.IAuthorRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongManagementService {


    @Autowired
    private ISongRepository songRepository;

    @Autowired
    private IAuthorRepository authorRepository;


    @Autowired
    public SongManagementService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public SongDto updateSong(Integer songId, SongDto songDto) {
        // Найти существующий трек
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // Обновить поля
        song.setSongName(songDto.getSongName());
        song.setYearOfCreation(songDto.getYearOfCreation());
        song.setTextSong(songDto.getTextSong());
        song.setSongCover(songDto.getSongCover());
        song.setAudioFile(songDto.getAudioFile());

        // Обновить автора: найти по имени или создать нового
        Author author = authorRepository.findByAuthorName(songDto.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(songDto.getAuthorName());
                    return authorRepository.save(newAuthor);
                });
        song.setAuthor(author);
        // Сохранить
        Song saved = songRepository.save(song);

        // Вернуть DTO
        SongDto result = new SongDto();
        result.setSongId(saved.getSongId());
        result.setSongName(saved.getSongName());
        result.setAuthorName(saved.getAuthor().getAuthorName());
        result.setYearOfCreation(saved.getYearOfCreation());
        result.setTextSong(saved.getTextSong());
        result.setSongCover(saved.getSongCover());
        result.setAudioFile(saved.getAudioFile());
        // комментарии можно не возвращать при обновлении
        return result;
    }

        public void deleteSong(Integer songId) {
        songRepository.deleteById(songId);
    }
}