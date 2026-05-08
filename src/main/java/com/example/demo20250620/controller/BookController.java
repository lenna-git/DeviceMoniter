package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Book;
import com.example.demo20250620.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(){return bookService.getBooks();}

    @PostMapping

    public Book creatBook(@RequestBody Book book){return bookService.saveBook(book);}

}
