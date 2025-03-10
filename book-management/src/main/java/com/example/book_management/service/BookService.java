package com.example.book_management.service;

import com.example.book_management.model.Book;
import com.example.book_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public List<Book> getAllBooksSortedByTitle() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    // retrieve all books
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // finding a book by its ID
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        return bookRepository.save(book);
    }

    //  a book with the given title already exists
    public boolean existsByName(String title) {
        return bookRepository.existsByTitle(title);
    }

    // a book with the given title exists, excluding a specific book ID
    public boolean existsByNameExcludingId(String title, Long id) {
        return bookRepository.existsByTitleAndIdNot(title, id);
    }


    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
