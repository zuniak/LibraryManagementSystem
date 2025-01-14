package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.AuthorValidationException;
import org.library.library_app.service.AuthorService;
import org.library.library_app.validationgroups.CreateAuthor;
import org.library.library_app.validationgroups.UpdateAuthor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;
    private final Validator validator;

    @PostMapping("/new")
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody @Valid AuthorDto author) {
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, CreateAuthor.class);
        if (!violations.isEmpty()) {
            throw new AuthorValidationException("Author create validation failed", violations);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addAuthor(author));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = service.getAllAuthorsDto();
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        Optional<AuthorDto> author = service.getAuthorDtoById(id);
        if (author.isPresent()) {
            return ResponseEntity.ok(author.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDto>> getAuthorBooks(@PathVariable Long id) {
        List<BookDto> authorsBooks = service.getAuthorBooksDto(id);
        if (authorsBooks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorsBooks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        service.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDto author) {
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, UpdateAuthor.class);
        if (!violations.isEmpty()) {
            throw new AuthorValidationException("Author update validation failed", violations);
        }
        service.updateAuthor(id, author);
        return ResponseEntity.ok().build();
    }
}
