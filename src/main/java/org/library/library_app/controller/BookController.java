package org.library.library_app.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.BookValidationException;
import org.library.library_app.service.BookService;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService service;
    private final Validator validator;

    @PostMapping("/new")
    public ResponseEntity<BookDto> addBook(@RequestBody @Valid BookDto book) {
        Set<ConstraintViolation<BookDto>> violations = validator.validate(book, CreateBook.class);
        if (!violations.isEmpty()) {
            throw new BookValidationException("Book create validation failed", violations);
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
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        Set<ConstraintViolation<BookDto>> violations = validator.validate(book, UpdateBook.class);
        if (!violations.isEmpty()) {
            throw new BookValidationException("Book update validation failed", violations);
        }
        service.updateBook(id, book);
        return ResponseEntity.noContent().build();
    }
}
