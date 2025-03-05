package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.exceptions.UserValidationException;
import org.library.library_app.service.LibraryUserService;
import org.library.library_app.tools.LibraryUserType;
import org.library.library_app.validationgroups.CreateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class LibraryUserController {
    private final LibraryUserService service;
    private final Validator validator;

    // TODO Get all readers that have borrowed a book and have not returned it yet, etc

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<LibraryUserDto> addEmployee(@RequestBody @Valid LibraryUserDto user) {
        Set<ConstraintViolation<LibraryUserDto>> violations = validator.validate(user, CreateUser.class);
        if (!violations.isEmpty()) {
            throw new UserValidationException("LibraryUser create validation failed", violations);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addUser(user, Set.of(LibraryUserType.EMPLOYEE)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<LibraryUserDto>> getAllUsers() {
        List<LibraryUserDto> users = service.getAllUsersDto();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/members")
    public ResponseEntity<List<LibraryUserDto>> getAllMembers() {
        List<LibraryUserDto> users = service.getAllUsersByType(LibraryUserType.MEMBER);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/employees")
    public ResponseEntity<List<LibraryUserDto>> getAllEmployees() {
        List<LibraryUserDto> users = service.getAllUsersByType(LibraryUserType.EMPLOYEE);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(path = {"/members/{username}", "/employees/{username}"})
    public ResponseEntity<LibraryUserDto> getUserById(@PathVariable String username) {
        LibraryUserDto user = service.getUserDtoByUsername(username);
        return ResponseEntity.ok(user);
    }
}
