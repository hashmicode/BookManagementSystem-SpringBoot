package com.example.book_management.service;

import com.example.book_management.model.Genre;
import com.example.book_management.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenresSortedByName() {
        return genreRepository.findAllByOrderByNameAsc();
    }
    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }


    public Optional<Genre> findGenreById(Long id) {
        return genreRepository.findById(id);
    }


    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }


    public boolean existsByName(String name) {
        return genreRepository.existsByName(name);
    }


    public boolean existsByNameAndNotId(String name, Long id) {
        return genreRepository.existsByNameAndIdNot(name, id);
    }


    public void deleteGenreById(Long id) throws DataIntegrityViolationException {
        if (findGenreById(id).isPresent()) {
            genreRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Invalid genre ID: " + id);
        }
    }
}
