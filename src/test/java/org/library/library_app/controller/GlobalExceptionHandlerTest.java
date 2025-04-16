package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleAuthorNotFoundException() {
        String message = "Author with id 1 not found";
        AuthorNotFoundException exception = new AuthorNotFoundException(1L);

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleBookNotFoundException() {
        String message = "Book with id 1 not found";
        BookNotFoundException exception = new BookNotFoundException(1L);

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
        String message = "Author validation failed.";
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
        String message = "Book validation failed.";
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
        String message = "Unknown category: invalidCategory";
        UnknownBookCategoryException unknownBookCategoryException = new UnknownBookCategoryException("invalidCategory");

        ResponseEntity<String> response = globalExceptionHandler.handleUnknownBookCategoryException(unknownBookCategoryException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void handleUnknownBookStatusException() {
        String message = "Unknown book status: invalidStatus";
        UnknownBookStatusException unknownBookStatusException = new UnknownBookStatusException("invalidStatus");

        ResponseEntity<String> response = globalExceptionHandler.handleUnknownBookStatusException(unknownBookStatusException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }


    @Test
    public void handleAuthorIdDoNotMatchException() {
        String message = "Author id does not match";
        AuthorIdDoNotMatchException exception = new AuthorIdDoNotMatchException();

        ResponseEntity<String> response = globalExceptionHandler.handleAuthorIdDoNotMatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleBookIdDoNotMatchException() {
        String message = "Book id does not match";
        BookIdDoNotMatchException exception = new BookIdDoNotMatchException();

        ResponseEntity<String> response = globalExceptionHandler.handleBookIdDoNotMatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleUserValidationException(){
        UserValidationException exception = new UserValidationException("User validation failed.", Set.of());

        ResponseEntity<String> response = globalExceptionHandler.handleUserValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User validation failed.", response.getBody());
    }

    @Test
    public void handleUsernameNotFoundException(){
        String message = "Username not found";
        UsernameNotFoundException exception = new UsernameNotFoundException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleUsernameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleUsernameTakenException(){
        String message = "Library user username already exists";
        UsernameTakenException exception = new UsernameTakenException("username");

        ResponseEntity<String> response = globalExceptionHandler.handleUsernameTakenException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleBookLimitExceededException(){
        String message = "Book limit exceeded";
        BookLimitExceededException exception = new BookLimitExceededException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleBookLimitExceededException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleDuplicateReservationException(){
        String message = "User username already requested reservation for book with id 1";
        DuplicateReservationException exception = new DuplicateReservationException("username", 1L);

        ResponseEntity<String> response = globalExceptionHandler.handleDuplicateReservationException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleBookReservationNotFoundException(){
        String message = "No reservation found for book with id 1";
        BookReservationNotFoundException exception = new BookReservationNotFoundException(1L);

        ResponseEntity<String> response = globalExceptionHandler.handleBookReservationNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void handleDueDateNotExtendedException(){
        String message = "Due date exceeded";
        DueDateNotExtendedException exception = new DueDateNotExtendedException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleDueDateNotExtendedException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}