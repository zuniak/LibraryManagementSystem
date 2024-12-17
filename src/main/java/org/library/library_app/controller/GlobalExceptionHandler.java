package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import org.library.library_app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(AuthorValidationException.class)
    public ResponseEntity<String> handleAuthorValidationException(AuthorValidationException exception) {
        String violationMessage = exception.getViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));

        String baseMessage = exception.getMessage();
        String responseMessage;

        if (baseMessage.isEmpty() && violationMessage.isEmpty()) {
            responseMessage = "Author validation failed";
        } else if (baseMessage.isEmpty()) {
            responseMessage = violationMessage;
        } else if (violationMessage.isEmpty()) {
            responseMessage = baseMessage;
        } else {
            responseMessage = baseMessage + ":\n" + violationMessage;
        }

        return ResponseEntity.badRequest().body(responseMessage);
    }

    @ExceptionHandler(BookValidationException.class)
    public ResponseEntity<String> handleBookValidationException(BookValidationException exception) {
        String violationMessage = exception.getViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));

        String baseMessage = exception.getMessage();
        String responseMessage;

        if (baseMessage.isEmpty() && violationMessage.isEmpty()) {
            responseMessage = "Book validation failed";
        } else if (baseMessage.isEmpty()) {
            responseMessage = violationMessage;
        } else if (violationMessage.isEmpty()) {
            responseMessage = baseMessage;
        } else {
            responseMessage = baseMessage + ":\n" + violationMessage;
        }

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
}