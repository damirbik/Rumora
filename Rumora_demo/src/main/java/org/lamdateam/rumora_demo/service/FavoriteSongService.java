// org.lamdateam.rumora_demo.service.FavoriteSongService.java

package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.SongDto;
import org.lamdateam.rumora_demo.entity.FavoriteSong;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.IFavoriteSongRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteSongService {

    private final IFavoriteSongRepository favoriteSongRepository;
    private final ISongRepository songRepository;

    @Autowired
    public FavoriteSongService(
            IFavoriteSongRepository favoriteSongRepository,
            ISongRepository songRepository
    ) {
        this.favoriteSongRepository = favoriteSongRepository;
        this.songRepository = songRepository;
    }

    public void addToFavorites(Long userId, Integer songId) {
        FavoriteSong favorite = new FavoriteSong();
        favorite.setUserId(userId);
        favorite.setSongId(songId);
        favoriteSongRepository.save(favorite);
    }

    public void removeFromFavorites(Long userId, Integer songId) {
        favoriteSongRepository.deleteById(new org.lamdateam.rumora_demo.entity.FavoriteSongId(userId, songId));
    }

    public List<SongDto> getFavoriteSongsByUserId(Long userId) {
        List<FavoriteSong> favorites = favoriteSongRepository.findByUserId(userId);

        // ✅ Используем songId НАПРЯМУЮ — НЕТ getSong()!
        List<Integer> songIds = favorites.stream()
                .map(FavoriteSong::getSongId)
                .collect(Collectors.toList());

        if (songIds.isEmpty()) {
            return List.of();
        }

        // ✅ Загружаем треки с автором
        List<Song> songs = songRepository.findAllByIdWithAuthor(songIds);

        return songs.stream().map(song -> {
            SongDto dto = new SongDto();
            dto.setSongId(song.getSongId());
            dto.setSongName(song.getSongName());
            dto.setAuthorName(song.getAuthor().getAuthorName()); // Теперь безопасно
            dto.setYearOfCreation(song.getYearOfCreation());
            dto.setTextSong(song.getTextSong());
            dto.setSongCover(song.getSongCover());
            return dto;
        }).collect(Collectors.toList());
    }

    public boolean isSongInFavorites(Long userId, Integer songId) {
        return favoriteSongRepository.existsByUserIdAndSongId(userId, songId);
    }
}