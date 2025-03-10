package com.example.book_management.repository;

import com.example.book_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByTitleAsc();
    // checking if a book with a given title exists
    boolean existsByTitle(String title);

    // checking if a book with a given title exists, excluding a specific book ID
    boolean existsByTitleAndIdNot(String title, Long id);
}
