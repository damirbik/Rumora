package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.SongSearchResultDto;
import org.lamdateam.rumora_demo.entity.Song;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongSearchService {

    private final ISongRepository songRepository;

    @Autowired
    public SongSearchService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<SongSearchResultDto> searchByTitle(String query) {
        List<Song> songs = songRepository.findBySongNameContaining(query);
        return mapToDto(songs);
    }

    public List<SongSearchResultDto> searchByArtist(String query) {
        List<Song> songs = songRepository.findByAuthorNameContaining(query);
        return mapToDto(songs);
    }

    private List<SongSearchResultDto> mapToDto(List<Song> songs) {
        List<SongSearchResultDto> dtos = new ArrayList<>();
        for (Song song : songs) {
            SongSearchResultDto dto = new SongSearchResultDto();
            dto.setSongId(song.getSongId());
            dto.setSongName(song.getSongName());
            dto.setAuthorName(song.getAuthor().getAuthorName());
            dto.setYearOfCreation(song.getYearOfCreation());
            dto.setSongCover(song.getSongCover());
            dtos.add(dto);
        }
        return dtos;
    }
}