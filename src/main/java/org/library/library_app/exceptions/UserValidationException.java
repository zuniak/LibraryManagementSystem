package org.library.library_app.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.library.library_app.dto.UserDto;

import java.util.Set;

@Getter
public class UserValidationException extends RuntimeException {
    private final Set<ConstraintViolation<UserDto>> violations;
    public UserValidationException(String message, Set<ConstraintViolation<UserDto>> violations) {
        super(message);
        this.violations = violations;
    }
}
