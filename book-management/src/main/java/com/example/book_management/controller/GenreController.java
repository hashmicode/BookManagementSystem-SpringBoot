package com.example.book_management.controller;

import com.example.book_management.model.Genre;
import com.example.book_management.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // show all genres
    @GetMapping
    public String listGenres(Model model) {
        model.addAttribute("genres", genreService.getAllGenresSortedByName());
        return "genres/listGenres";
    }

    // form to add a new genre
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genres/addGenre";
    }

    @PostMapping
    public String saveGenre(@Valid @ModelAttribute Genre genre, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "genres/addGenre";
        }

        if (genreService.existsByName(genre.getName())) {
            model.addAttribute("errorMessage", "Genre name already exists.");
            return "redirect:/genres/add";
        }

        genreService.saveGenre(genre);
        model.addAttribute("successMessage", "Genre added successfully!");
        return "redirect:/genres";
    }

    // edit a genre
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Genre genre = genreService.findGenreById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + id));
        model.addAttribute("genre", genre);
        return "genres/editGenre";
    }

    @PostMapping("/update/{id}")
    public String updateGenre(@PathVariable("id") long id,
                              @Valid @ModelAttribute Genre genre,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "genres/editGenre";
        }

        // Check for duplicate genre name excluding the current genre
        if (genreService.existsByNameAndNotId(genre.getName(), id)) {
            model.addAttribute("errorMessage", "Genre name already exists.");
            return "genres/editGenre"; // Stay on the same page
        }

        genreService.saveGenre(genre);
        model.addAttribute("successMessage", "Genre updated successfully!");
        return "redirect:/genres";
    }

    // delete a genre by ID
    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            genreService.deleteGenreById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Genre deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete genre as it is referenced by books.");
        }
        return "redirect:/genres";
    }
}
