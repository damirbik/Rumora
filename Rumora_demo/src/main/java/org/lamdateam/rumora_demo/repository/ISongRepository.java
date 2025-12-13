package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ISongRepository extends JpaRepository<Song, Integer> {

    @Query("SELECT s FROM Song s JOIN s.author a WHERE UPPER(s.songName) LIKE UPPER(CONCAT('%', :query, '%'))")
    List<Song> findBySongNameContaining(@Param("query") String query);

    @Query("SELECT s FROM Song s JOIN s.author a WHERE UPPER(a.authorName) LIKE UPPER(CONCAT('%', :query, '%'))")
    List<Song> findByAuthorNameContaining(@Param("query") String query);
}