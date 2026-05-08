package com.example.demo20250620.repository;

import com.example.demo20250620.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}