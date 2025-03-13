package org.library.library_app.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.exceptions.UserValidationException;
import org.library.library_app.service.LibraryUserService;
import org.library.library_app.tools.LibraryUserType;
import org.library.library_app.validationgroups.CreateUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final LibraryUserService userService;
    private final Validator validator;

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody LibraryUserDto libraryUserDto) {
        libraryUserDto.setPassword(passwordEncoder.encode(libraryUserDto.getPassword()));

        Set<ConstraintViolation<LibraryUserDto>> violations = validator.validate(libraryUserDto, CreateUser.class);
        if (!violations.isEmpty()) {
            throw new UserValidationException("Library member registration failed", violations);
        }
        userService.addUser(libraryUserDto, Set.of(LibraryUserType.MEMBER));
        return ResponseEntity.status(HttpStatus.CREATED).body("Library member " + libraryUserDto.getUsername() + " registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("Library user " + username + " logged in.");
    }
}

