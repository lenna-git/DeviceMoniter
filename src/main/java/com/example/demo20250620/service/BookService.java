package com.example.demo20250620.service;


import com.example.demo20250620.entity.Book;
import com.example.demo20250620.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    public List<Book> getBooks(){return bookRepository.findAll();}
    public  Book saveBook(Book book){return bookRepository.save(book);}
}
