package org.library.library_app.controller;


import jakarta.validation.Valid;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Book;
import org.library.library_app.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.library.library_app.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;


    @PostMapping("/new")
    public ResponseEntity<BookDto> addBook(@RequestBody @Valid BookDto bookDto) {
        Book savedBook = service.saveBook(bookDto);

        BookDto savedBookDTO = new BookDto(savedBook.getTitle(), savedBook.getAuthor());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBookDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(){
        System.out.println("Get all books");
        List<BookDto> booksDto= service.getAllBooks().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.of(Optional.of(booksDto));
    }
}
