package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a WHERE UPPER(a.authorName) = UPPER(:authorName)")
    Optional<Author> findByAuthorName(@Param("authorName") String authorName);

    boolean existsByAuthorName(String authorName);
}