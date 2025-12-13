package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongManagementService {

    private final ISongRepository songRepository;

    @Autowired
    public SongManagementService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song updateSong(Song song) {
        return songRepository.save(song);
    }

    public void deleteSong(Integer songId) {
        songRepository.deleteById(songId);
    }
}