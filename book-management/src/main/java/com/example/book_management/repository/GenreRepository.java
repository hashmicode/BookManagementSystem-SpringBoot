package com.example.book_management.repository;

import com.example.book_management.model.Book;
import com.example.book_management.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    // if a genre exists by name
    boolean existsByName(String name);

    // if a genre exists by name excluding a specific ID
    boolean existsByNameAndIdNot(String name, Long id);

    List<Genre> findAllByOrderByNameAsc();
}
