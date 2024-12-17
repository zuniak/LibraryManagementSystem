package org.library.library_app.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.library.library_app.dto.AuthorDto;

import java.util.Set;

@Getter
public class AuthorValidationException extends RuntimeException {

    private Set<ConstraintViolation<AuthorDto>> violations;
    public AuthorValidationException(String message, Set<ConstraintViolation<AuthorDto>> violations) {
        super(message);
        this.violations = violations;
    }

    public AuthorValidationException(String message) {
        super(message);
    }
}
