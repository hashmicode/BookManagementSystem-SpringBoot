package com.example.book_management.service;

import com.example.book_management.model.Publisher;
import com.example.book_management.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishersSortedByName() {
        return publisherRepository.findAllByOrderByNameAsc();
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }


    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }


    public Publisher savePublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }


    public void deleteById(Long id) {
        publisherRepository.deleteById(id);
    }


    public boolean existsByName(String name) {
        return publisherRepository.existsByName(name);
    }


    public boolean existsByNameExcludingId(String name, Long id) {
        return publisherRepository.existsByNameAndIdNot(name, id);
    }
}
