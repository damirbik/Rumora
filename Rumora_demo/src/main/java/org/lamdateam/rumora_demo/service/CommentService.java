package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.repository.ICommentRepository;
import org.lamdateam.rumora_demo.repository.ISongRepository;
import org.lamdateam.rumora_demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final ICommentRepository commentRepository;
    private final ISongRepository songRepository;
    private final IUserRepository userRepository;

    @Autowired
    public CommentService(
            ICommentRepository commentRepository,
            ISongRepository songRepository,
            IUserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    /**
     * Добавляет комментарий от пользователя к треку.
     *
     * @param songId      ID трека
     * @param userId      ID пользователя (из токена, не из клиента!)
     * @param commentText текст комментария
     * @return сохранённый комментарий
     */
    public Comment addComment(Integer songId, Long userId, String commentText) {
        // 🔒 Проверка существования трека (опционально, но рекомендуется)
        if (!songRepository.existsById(songId)) {
            throw new RuntimeException("Song with ID " + songId + " not found");
        }

        // 🔒 Проверка существования пользователя (опционально)
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }

        Comment comment = new Comment();
        comment.setSongId(songId);
        comment.setUserId(userId);
        comment.setCommentText(commentText);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment with ID " + commentId + " not found");
        }
        commentRepository.deleteById(commentId);
    }
}