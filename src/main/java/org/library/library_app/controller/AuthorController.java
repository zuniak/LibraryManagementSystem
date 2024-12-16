package org.library.library_app.controller;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.service.AuthorService;
import org.library.library_app.validationgroups.CreateAuthor;
import org.library.library_app.validationgroups.UpdateAuthor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;
    private final Validator validator;

    @PostMapping("/new")
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody @Valid AuthorDto author) {
        if (!validator.validate(author, CreateAuthor.class).isEmpty()) {
            return ResponseEntity.badRequest().build();
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

    @GetMapping("{id}/books")
    public ResponseEntity<List<BookDto>> getAuthorBooks(@PathVariable Long id) {
        try {
            List<BookDto> authorsBooks = service.getAuthorBooksDto(id);
            if (authorsBooks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(authorsBooks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        try {
            if (service.deleteAuthor(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDto author) {
        validator.validate(author, UpdateAuthor.class);

        try {
            if (service.updateAuthor(id, author)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
