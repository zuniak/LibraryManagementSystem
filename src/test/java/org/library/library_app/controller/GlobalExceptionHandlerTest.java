package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleAuthorNotFoundException() {
        String message = "Author not found";
        AuthorNotFoundException exception = new AuthorNotFoundException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleBookNotFoundException() {
        String message = "Book not found";
        BookNotFoundException exception = new BookNotFoundException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleBookNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleAuthorValidationException_WhenMessageEmptyAndNoViolations() {
        AuthorValidationException authorValidationException = new AuthorValidationException("", Set.of());

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorValidationException(authorValidationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Author validation failed", response.getBody());
    }

    @Test
    void handleAuthorValidationException_WhenMessageEmptyAndViolations() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<AuthorDto> violation = mock(ConstraintViolation.class);
        String violation_message = "Violation message 1";

        Set<ConstraintViolation<AuthorDto>> violations = Set.of(violation);

        when(violation.getMessage()).thenReturn(violation_message);

        AuthorValidationException exception = new AuthorValidationException("", violations);

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(violation_message, response.getBody());
    }

    @Test
    void handleAuthorValidationException_WhenMessageNotEmptyAndNoViolations() {
        String message = "Author validation failed";
        AuthorValidationException authorValidationException = new AuthorValidationException(message, Set.of());

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorValidationException(authorValidationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleAuthorValidationException_WhenMessageNotEmptyAndViolations() {
        String message = "Author validation failed";

        @SuppressWarnings("unchecked")
        ConstraintViolation<AuthorDto> violation = mock(ConstraintViolation.class);
        String violation_message = "Name cannot be null";

        Set<ConstraintViolation<AuthorDto>> violations = Set.of(violation);

        when(violation.getMessage()).thenReturn(violation_message);

        AuthorValidationException exception = new AuthorValidationException(message, violations);

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String expectedMessage = message + ":\n" + violation_message;
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleBookValidationException_WhenMessageEmptyAndNoViolations() {
        BookValidationException bookValidationException = new BookValidationException("", Set.of());

        ResponseEntity<String> response = globalExceptionHandler.handleBookValidationException(bookValidationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book validation failed", response.getBody());
    }

    @Test
    void handleBookValidationException_WhenMessageEmptyAndViolations() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<BookDto> violation = mock(ConstraintViolation.class);
        String violation_message = "Violation message";

        Set<ConstraintViolation<BookDto>> violations = Set.of(violation);

        when(violation.getMessage()).thenReturn(violation_message);

        BookValidationException exception = new BookValidationException("", violations);

        ResponseEntity<String> response = globalExceptionHandler.handleBookValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(violation_message, response.getBody());
    }

    @Test
    void handleBookValidationException_WhenMessageNotEmptyAndNoViolations() {
        String message = "Book validation failed";
        BookValidationException bookValidationException = new BookValidationException(message, Set.of());

        ResponseEntity<String> response = globalExceptionHandler.handleBookValidationException(bookValidationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleBookValidationException_WhenMessageNotEmptyAndViolations() {
        String message = "Book validation failed";

        @SuppressWarnings("unchecked")
        ConstraintViolation<BookDto> violation = mock(ConstraintViolation.class);
        String violation_message = "Name cannot be null";

        Set<ConstraintViolation<BookDto>> violations = Set.of(violation);

        when(violation.getMessage()).thenReturn(violation_message);

        BookValidationException exception = new BookValidationException(message, violations);

        ResponseEntity<String> response = globalExceptionHandler.handleBookValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        String expectedMessage = message + ":\n" + violation_message;
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleUnknownBookCategoryException() {
        String message = "Unknown book category";
        UnknownBookCategoryException unknownBookCategoryException = new UnknownBookCategoryException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleUnknownBookCategoryException(unknownBookCategoryException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleUnknownBookStatusException() {
        String message = "Unknown book status";
        UnknownBookStatusException unknownBookStatusException = new UnknownBookStatusException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleUnknownBookStatusException(unknownBookStatusException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }


    @Test
    public void handleAuthorIdDoNotMatchException() {
        String message = "Author id do not match";
        AuthorIdDoNotMatchException exception = new AuthorIdDoNotMatchException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorIdDoNotMatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleBookIdDoNotMatchException() {
        String message = "Book id do not match";
        BookIdDoNotMatchException exception = new BookIdDoNotMatchException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleBookIdDoNotMatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}