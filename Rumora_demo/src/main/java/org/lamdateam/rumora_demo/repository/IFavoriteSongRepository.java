package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.FavoriteSong;
import org.lamdateam.rumora_demo.entity.FavoriteSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteSongRepository extends JpaRepository<FavoriteSong, FavoriteSongId> {
    List<FavoriteSong> findByUserId(Long userId);

}