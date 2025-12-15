package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.entity.Author;
import org.lamdateam.rumora_demo.entity.FavoriteSong;
import org.lamdateam.rumora_demo.entity.FavoriteSongId;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.IAuthorRepository;
import org.lamdateam.rumora_demo.repository.IFavoriteSongRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteSongService {

    private final IFavoriteSongRepository favoriteSongRepository;
    private final ISongRepository songRepository;
    private final IAuthorRepository authorRepository;

    @Autowired
    public FavoriteSongService(
            IFavoriteSongRepository favoriteSongRepository,
            ISongRepository songRepository,
            IAuthorRepository authorRepository
    ) {
        this.favoriteSongRepository = favoriteSongRepository;
        this.songRepository = songRepository;
        this.authorRepository = authorRepository;
    }

    public void addToFavorites(Long userId, Integer songId) {
        FavoriteSong favorite = new FavoriteSong();
        favorite.setUserId(userId);
        favorite.setSongId(songId);
        favoriteSongRepository.save(favorite);
    }

    public void removeFromFavorites(Long userId, Integer songId) {
        favoriteSongRepository.deleteById(new FavoriteSongId(userId, songId));
    }

    public List<SongDto> getFavoriteSongsByUserId(Long userId) {
        // Получаем список FavoriteSong по userId
        List<FavoriteSong> favorites = favoriteSongRepository.findByUserId(userId);

        // Извлекаем ID треков
        List<Integer> songIds = favorites.stream()
                .map(fav -> fav.getSong().getSongId())
                .collect(Collectors.toList());

        // Получаем треки из БД
        List<Song> songs = songRepository.findAllById(songIds);

        // Преобразуем в SongDto
        List<SongDto> dtos = new ArrayList<>();
        for (Song song : songs) {
            SongDto dto = new SongDto();
            dto.setSongId(song.getSongId());
            dto.setSongName(song.getSongName());
            dto.setAuthorName(song.getAuthor().getAuthorName());
            dto.setYearOfCreation(song.getYearOfCreation());
            dto.setTextSong(song.getTextSong());
            dto.setSongCover(song.getSongCover());
            // Здесь можно заполнить комментарии, если нужно
            dtos.add(dto);
        }
        return dtos;
    }
}