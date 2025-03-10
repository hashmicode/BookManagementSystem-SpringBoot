package com.example.book_management;

import com.example.book_management.service.GenreService;
import com.example.book_management.model.Genre;
import com.example.book_management.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenreServiceTest {

    @InjectMocks
    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllGenres() {
        when(genreRepository.findAll()).thenReturn(List.of(new Genre()));
        List<Genre> genres = genreService.findAllGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    public void testFindGenreById() {
        Long genreId = 1L;
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(new Genre()));
        Optional<Genre> genre = genreService.findGenreById(genreId);
        assertTrue(genre.isPresent());
        verify(genreRepository, times(1)).findById(genreId);
    }

    @Test
    public void testSaveGenre() {
        Genre genre = new Genre();
        when(genreRepository.save(genre)).thenReturn(genre);
        Genre savedGenre = genreService.saveGenre(genre);
        assertNotNull(savedGenre);
        verify(genreRepository, times(1)).save(genre);
    }

    @Test
    public void testExistsByGenreName() {
        String genreName = "Fiction";
        when(genreRepository.existsByName(genreName)).thenReturn(true);
        assertTrue(genreService.existsByName(genreName));
        verify(genreRepository, times(1)).existsByName(genreName);
    }

    @Test
    public void testFindGenreByIdNotFound() {
        Long nonExistingId = 999L;
        when(genreRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<Genre> genre = genreService.findGenreById(nonExistingId);
        assertFalse(genre.isPresent());
    }

    @Test
    public void testDeleteGenreById() {
        Long genreId = 1L;
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName("Fiction");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        doNothing().when(genreRepository).deleteById(genreId);
        genreService.deleteGenreById(genreId);
        verify(genreRepository, times(1)).deleteById(genreId);
    }

}
