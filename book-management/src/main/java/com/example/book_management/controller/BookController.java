package com.example.book_management.controller;

import com.example.book_management.model.Book;
import com.example.book_management.model.Genre;
import com.example.book_management.model.Publisher;
import com.example.book_management.service.BookService;
import com.example.book_management.service.GenreService;
import com.example.book_management.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final GenreService genreService;
    private final PublisherService publisherService;

    @Autowired
    public BookController(BookService bookService, GenreService genreService, PublisherService publisherService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.publisherService = publisherService;
    }

    // show list of books
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooksSortedByTitle());

        return "books/listBook";
    }

    // show form to add a new book
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        List<Genre> genres = genreService.findAllGenres();
        List<Publisher> publishers = publisherService.findAll();

        System.out.println("Genres: " + genres);
        System.out.println("Publishers: " + publishers);

        model.addAttribute("genres", genreService.getAllGenresSortedByName());
        model.addAttribute("publishers", publisherService.getAllPublishersSortedByName());
        return "books/addBook";
    }

    @PostMapping
    public String saveBook(@Valid @ModelAttribute Book book, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("genres", genreService.findAllGenres());
            model.addAttribute("publishers", publisherService.findAll());
            return "books/addBook";
        }

        if (bookService.existsByName(book.getTitle())) {
            model.addAttribute("errorMessage", "Book with this title already exists.");
            model.addAttribute("genres", genreService.findAllGenres());
            model.addAttribute("publishers", publisherService.findAll());
            return "books/addBook";
        }

        bookService.save(book);
        redirectAttributes.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/books";
    }

    // form to edit a book
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("Book not found: " + id));
        model.addAttribute("book", book);
        model.addAttribute("genres", genreService.findAllGenres());
        model.addAttribute("publishers", publisherService.findAll());
        return "books/editBook";
    }

    @PostMapping("/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute Book book,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("genres", genreService.findAllGenres());
            model.addAttribute("publishers", publisherService.findAll());
            return "books/editBook";
        }

        if (bookService.existsByNameExcludingId(book.getTitle(), id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book with this title already exists.");
            return "redirect:/books/edit/" + id;
        }

        bookService.save(book);
        return "redirect:/books";
    }


    // view book details
    @GetMapping("/details/{id}")
    public String viewBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("Book not found: " + id));
        model.addAttribute("book", book);
        return "books/bookDetails";
    }

    // delete a book by ID
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
