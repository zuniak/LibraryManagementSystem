package org.library.library_app.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.library.library_app.dto.LibraryUserDto;

import java.util.Set;

@Getter
public class UserValidationException extends RuntimeException {
    private final Set<ConstraintViolation<LibraryUserDto>> violations;
    public UserValidationException(String message, Set<ConstraintViolation<LibraryUserDto>> violations) {
        super(message);
        this.violations = violations;
    }
}
