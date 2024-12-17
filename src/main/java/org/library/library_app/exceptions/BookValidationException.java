package org.library.library_app.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.library.library_app.dto.BookDto;

import java.util.Set;

@Getter
public class BookValidationException extends RuntimeException {
    private Set<ConstraintViolation<BookDto>> violations;

    public BookValidationException(String message, Set<ConstraintViolation<BookDto>> violations) {
        super(message);
        this.violations = violations;
    }

    public BookValidationException(String message) {super(message);}
}
