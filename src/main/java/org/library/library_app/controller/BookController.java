package org.library.library_app.controller;


import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.BookDto;
import org.library.library_app.service.BookService;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService service;
    private final Validator validator;

    @PostMapping("/new")
    public ResponseEntity<BookDto> addBook(@RequestBody @Valid BookDto book) {
        if (!validator.validate(book, CreateBook.class).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addBook(book));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> booksData = service.getAllBooksDto();
        if (booksData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(booksData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        Optional<BookDto> book = service.getBookDto(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            if (service.deleteBook(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        if (!validator.validate(book, UpdateBook.class).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            if (service.updateBook(id, book)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
