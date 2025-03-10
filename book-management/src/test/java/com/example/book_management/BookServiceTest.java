package com.example.book_management;

import com.example.book_management.service.BookService;
import com.example.book_management.model.Book;
import com.example.book_management.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("To Kill a Mockingbird");
        book1.setAuthor("Harper Lee");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> books = bookService.findAll();

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("To Kill a Mockingbird", books.get(0).getTitle());
        assertEquals("1984", books.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testFindBookById() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Pride and Prejudice");
        book.setAuthor("Jane Austen");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.findById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals("Pride and Prejudice", foundBook.get().getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        assertNotNull(savedBook);
        assertEquals("The Great Gatsby", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testExistsByName() {
        String title = "Moby Dick";

        when(bookRepository.existsByTitle(title)).thenReturn(true);

        assertTrue(bookService.existsByName(title));
        verify(bookRepository, times(1)).existsByTitle(title);
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("The Catcher in the Rye");
        existingBook.setAuthor("J.D. Salinger");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        existingBook.setTitle("The Catcher in the Rye (Updated)");
        Book updatedBook = bookService.save(existingBook);

        assertNotNull(updatedBook);
        assertEquals("The Catcher in the Rye (Updated)", updatedBook.getTitle());
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testFindBookByIdNotFound() {
        Long bookId = 2L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> book = bookService.findById(bookId);

        assertFalse(book.isPresent());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testDeleteBookById() {
        Long bookId = 3L;

        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }
    @Test
    public void testSaveBookWithInvalidData() {
        Book invalidBook = new Book();
        invalidBook.setAuthor("Author Without Title");

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.save(invalidBook);
        });
    }

    @Test
    public void testDeleteNonExistingBook() {
        Long nonExistingId = 999L;
        doThrow(new IllegalArgumentException("Book not found")).when(bookRepository).deleteById(nonExistingId);

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteById(nonExistingId);
        });
    }

}
