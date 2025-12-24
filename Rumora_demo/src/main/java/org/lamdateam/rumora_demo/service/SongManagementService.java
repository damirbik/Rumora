package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.FileUploadResponseDto;
import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.entity.Author;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.IAuthorRepository;
import org.lamdateam.rumora_demo.repository.IFavoriteSongRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SongManagementService {

    private final ISongRepository songRepository;
    private final IAuthorRepository authorRepository;
    private final FileStorageService fileStorageService;
    private final IFavoriteSongRepository favoriteSongRepository;

    @Autowired
    public SongManagementService(
            ISongRepository songRepository,
            IAuthorRepository authorRepository,
            FileStorageService fileStorageService, IFavoriteSongRepository favoriteSongRepository
    ) {
        this.songRepository = songRepository;
        this.authorRepository = authorRepository;
        this.fileStorageService = fileStorageService;
        this.favoriteSongRepository = favoriteSongRepository;
    }

    /**
     * Обновляет существующий трек (для админа/модератора)
     */
    public SongDto updateSong(Integer songId, SongDto songDto) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        // Обновляем ТОЛЬКО те поля, которые переданы (не null)
        if (songDto.getSongName() != null) {
            song.setSongName(songDto.getSongName());
        }
        if (songDto.getYearOfCreation() != null) {
            song.setYearOfCreation(songDto.getYearOfCreation());
        }
        if (songDto.getTextSong() != null) {
            song.setTextSong(songDto.getTextSong());
        }
        // ⚠️ НЕ обновляем songCover и audioFile здесь!
        // Они должны обновляться отдельно

        // Обновляем автора, если передано
        if (songDto.getAuthorName() != null && !songDto.getAuthorName().trim().isEmpty()) {
            Author author = authorRepository.findByAuthorName(songDto.getAuthorName())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setAuthorName(songDto.getAuthorName());
                        return authorRepository.save(newAuthor);
                    });
            song.setAuthor(author);
        }

        Song saved = songRepository.save(song);
        return convertToDto(saved);
    }

    public SongDto updateSongCover(Integer songId, MultipartFile newCover) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        FileUploadResponseDto coverResponse = fileStorageService.storeCoverFile(newCover);
        song.setSongCover(coverResponse.getFileDownloadUri());

        Song saved = songRepository.save(song);
        return convertToDto(saved);
    }

    public SongDto updateSongAudio(Integer songId, MultipartFile newAudio) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        FileUploadResponseDto audioResponse = fileStorageService.storeAudioFile(newAudio);
        song.setAudioFile(audioResponse.getFileDownloadUri());

        Song saved = songRepository.save(song);
        return convertToDto(saved);
    }



    /**
     * Удаляет трек по ID
     */
    public void deleteSong(Integer songId) {
        if (!songRepository.existsById(songId)) {
            throw new RuntimeException("Трек не найден");
        }
        favoriteSongRepository.deleteBySongId(songId); // ← явно
        songRepository.deleteById(songId);
    }

    /**
     * Добавляет новый трек с загрузкой файлов (только для админа)
     */
    public SongDto addSong(
            String songName,
            String authorName,
            Integer yearOfCreation,
            String textSong,
            MultipartFile songCover,
            MultipartFile audioFile
    ) {
        // 1. Сохраняем файлы
        FileUploadResponseDto coverResponse = fileStorageService.storeCoverFile(songCover);
        FileUploadResponseDto audioResponse = fileStorageService.storeAudioFile(audioFile);

        // 2. Находим или создаём автора
        Author author = authorRepository.findByAuthorName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(authorName);
                    return authorRepository.save(newAuthor);
                });

        // 3. Создаём трек
        Song song = new Song();
        song.setSongName(songName);
        song.setAuthor(author);
        song.setYearOfCreation(yearOfCreation);
        song.setTextSong(textSong != null ? textSong : "");
        song.setSongCover(coverResponse.getFileDownloadUri());
        song.setAudioFile(audioResponse.getFileDownloadUri());

        Song saved = songRepository.save(song);

        return convertToDto(saved);
    }

    // Вспомогательный метод для маппинга
    private SongDto convertToDto(Song song) {
        SongDto dto = new SongDto();
        dto.setSongId(song.getSongId());
        dto.setSongName(song.getSongName());
        dto.setAuthorName(song.getAuthor().getAuthorName());
        dto.setYearOfCreation(song.getYearOfCreation());
        dto.setTextSong(song.getTextSong());
        dto.setSongCover(song.getSongCover());
        dto.setAudioFile(song.getAudioFile());
        return dto;
    }
}