package com.example.book_management;

import com.example.book_management.service.PublisherService;
import com.example.book_management.model.Publisher;
import com.example.book_management.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherRepository publisherRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllPublishers() {
        Publisher publisher1 = new Publisher();
        publisher1.setId(1L);
        publisher1.setName("Penguin Random House");
        publisher1.setAddress("1745 Broadway, New York, NY");

        Publisher publisher2 = new Publisher();
        publisher2.setId(2L);
        publisher2.setName("HarperCollins");
        publisher2.setAddress("195 Broadway, New York, NY");

        when(publisherRepository.findAll()).thenReturn(List.of(publisher1, publisher2));

        List<Publisher> publishers = publisherService.findAll();

        assertNotNull(publishers);
        assertEquals(2, publishers.size());
        assertEquals("Penguin Random House", publishers.get(0).getName());
        assertEquals("HarperCollins", publishers.get(1).getName());
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    public void testFindPublisherById() {
        Long publisherId = 1L;
        Publisher publisher = new Publisher();
        publisher.setId(publisherId);
        publisher.setName("Oxford University Press");
        publisher.setAddress("Great Clarendon St, Oxford, UK");

        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));

        Optional<Publisher> foundPublisher = publisherService.findById(publisherId);

        assertTrue(foundPublisher.isPresent());
        assertEquals("Oxford University Press", foundPublisher.get().getName());
        verify(publisherRepository, times(1)).findById(publisherId);
    }

    @Test
    public void testSavePublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("Simon & Schuster");
        publisher.setAddress("1230 Avenue of the Americas, New York, NY");

        when(publisherRepository.save(publisher)).thenReturn(publisher);

        Publisher savedPublisher = publisherService.savePublisher(publisher);

        assertNotNull(savedPublisher);
        assertEquals("Simon & Schuster", savedPublisher.getName());
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    public void testExistsByPublisherName() {
        String publisherName = "Hachette Book Group";

        when(publisherRepository.existsByName(publisherName)).thenReturn(true);

        assertTrue(publisherService.existsByName(publisherName));
        verify(publisherRepository, times(1)).existsByName(publisherName);
    }
    @Test
    public void testDeleteNonExistingPublisher() {
        Long nonExistingId = 999L;
        doThrow(new IllegalArgumentException("Publisher not found")).when(publisherRepository).deleteById(nonExistingId);

        assertThrows(IllegalArgumentException.class, () -> {
            publisherService.deleteById(nonExistingId);
        });
    }

    @Test
    public void testDeletePublisherById() {
        Long publisherId = 3L;

        doNothing().when(publisherRepository).deleteById(publisherId);

        publisherService.deleteById(publisherId);

        verify(publisherRepository, times(1)).deleteById(publisherId);
    }
}
