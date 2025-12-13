package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final ICommentRepository commentRepository;

    @Autowired
    public CommentService(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}