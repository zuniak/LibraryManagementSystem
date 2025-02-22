package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.UserDto;
import org.library.library_app.exceptions.UserValidationException;
import org.library.library_app.service.UserService;
import org.library.library_app.tools.UserType;
import org.library.library_app.validationgroups.CreateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final Validator validator;

    // TODO Get all readers that have borrowed a book and have not returned it yet, etc

    @PostMapping("/new")
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto user) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user, CreateUser.class);
        if (!violations.isEmpty()) {
            throw new UserValidationException("User create validation failed", violations);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = service.getAllUsersDto();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/members")
    public ResponseEntity<List<UserDto>> getAllMembers() {
        List<UserDto> users = service.getAllUsersByType(UserType.MEMBER);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserDto>> getAllEmployees() {
        List<UserDto> users = service.getAllUsersByType(UserType.EMPLOYEE);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = {"/members/{id}", "/employees/{id}"})
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = Optional.of(service.getUserById(id));
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
}
