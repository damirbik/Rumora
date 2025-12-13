package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.FavoriteSong;
import org.lamdateam.rumora_demo.entity.FavoriteSongId;
import org.lamdateam.rumora_demo.repository.IFavoriteSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteSongService {

    private final IFavoriteSongRepository favoriteSongRepository;

    @Autowired
    public FavoriteSongService(IFavoriteSongRepository favoriteSongRepository) {
        this.favoriteSongRepository = favoriteSongRepository;
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
}