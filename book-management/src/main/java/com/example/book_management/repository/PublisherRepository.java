package com.example.book_management.repository;

import com.example.book_management.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    List<Publisher> findAllByOrderByNameAsc();
}
