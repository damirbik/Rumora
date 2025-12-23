package org.lamdateam.rumora_demo.repository;

import org.lamdateam.rumora_demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findBySongId(Integer songId);

    boolean existsById(Integer commentId);
}