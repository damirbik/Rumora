// org.lamdateam.rumora_demo.repository.ISongRepository.java

package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISongRepository extends JpaRepository<Song, Integer> {

    @Query("SELECT s FROM Song s JOIN FETCH s.author WHERE s.songId IN :ids")
    List<Song> findAllByIdWithAuthor(@Param("ids") List<Integer> ids);
}