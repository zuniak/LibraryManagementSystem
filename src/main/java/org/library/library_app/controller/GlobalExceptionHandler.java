package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import org.library.library_app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handleAuthorNotFoundException(AuthorNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    private static <T> String getResponseMessage(String baseMessage, Set<ConstraintViolation<T>> violations, String defaultMessage) {
        String violationMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));

        String responseMessage;

        if (baseMessage.isEmpty() && violationMessage.isEmpty()) {
            responseMessage = defaultMessage;
        } else if (baseMessage.isEmpty()) {
            responseMessage = violationMessage;
        } else if (violationMessage.isEmpty()) {
            responseMessage = baseMessage;
        } else {
            responseMessage = baseMessage + ":\n" + violationMessage;
        }
        return responseMessage;
    }

    @ExceptionHandler(AuthorValidationException.class)
    public ResponseEntity<String> handleAuthorValidationException(AuthorValidationException exception) {
        String responseMessage = getResponseMessage(exception.getMessage(), exception.getViolations(), "Author validation failed");
        return ResponseEntity.badRequest().body(responseMessage);
    }

    @ExceptionHandler(BookValidationException.class)
    public ResponseEntity<String> handleBookValidationException(BookValidationException exception) {
        String responseMessage = getResponseMessage(exception.getMessage(), exception.getViolations(), "Book validation failed");

        return ResponseEntity.badRequest().body(responseMessage);
    }

    @ExceptionHandler(UnknownBookCategoryException.class)
    public ResponseEntity<String> handleUnknownBookCategoryException(UnknownBookCategoryException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(UnknownBookStatusException.class)
    public ResponseEntity<String> handleUnknownBookStatusException(UnknownBookStatusException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(AuthorIdDoNotMatchException.class)
    public ResponseEntity<String> handleAuthorIdDoNotMatchException(AuthorIdDoNotMatchException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(BookIdDoNotMatchException.class)
    public ResponseEntity<String> handleBookIdDoNotMatchException(BookIdDoNotMatchException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}