package org.lamdateam.rumora_demo.service;

import org.lamdateam.rumora_demo.dto.AuthorDto;
import org.lamdateam.rumora_demo.entity.Author;
import org.lamdateam.rumora_demo.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления авторами (исполнителями).
 */
@Service
public class AuthorService {

    private final IAuthorRepository authorRepository;

    @Autowired
    public AuthorService(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Получает список всех авторов.
     *
     * @return список DTO авторов
     */
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> dtos = new ArrayList<>();
        for (Author author : authors) {
            AuthorDto dto = new AuthorDto();
            dto.setAuthorId(author.getAuthorId());
            dto.setAuthorName(author.getAuthorName());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * Находит автора по ID.
     *
     * @param authorId ID автора
     * @return DTO автора
     * @throws RuntimeException если автор не найден
     */
    public AuthorDto getAuthorById(Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор с ID " + authorId + " не найден"));
        AuthorDto dto = new AuthorDto();
        dto.setAuthorId(author.getAuthorId());
        dto.setAuthorName(author.getAuthorName());
        return dto;
    }

    /**
     * Находит автора по имени (регистронезависимо).
     *
     * @param authorName имя автора
     * @return DTO автора, если найден
     * @throws RuntimeException если автор не найден
     */
    public AuthorDto getAuthorByName(String authorName) {
        Optional<Author> authorOpt = authorRepository.findByAuthorName(authorName);
        if (authorOpt.isEmpty()) {
            throw new RuntimeException("Автор '" + authorName + "' не найден");
        }
        Author author = authorOpt.get();
        AuthorDto dto = new AuthorDto();
        dto.setAuthorId(author.getAuthorId());
        dto.setAuthorName(author.getAuthorName());
        return dto;
    }

    /**
     * Создаёт нового автора.
     *
     * @param authorName имя нового автора
     * @return DTO созданного автора
     * @throws RuntimeException если автор с таким именем уже существует
     */
    public AuthorDto createAuthor(String authorName) {
        if (authorRepository.existsByAuthorName(authorName)) {
            throw new RuntimeException("Автор с именем '" + authorName + "' уже существует");
        }
        Author author = new Author();
        author.setAuthorName(authorName);
        Author savedAuthor = authorRepository.save(author);

        AuthorDto dto = new AuthorDto();
        dto.setAuthorId(savedAuthor.getAuthorId());
        dto.setAuthorName(savedAuthor.getAuthorName());
        return dto;
    }
}