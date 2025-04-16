package org.library.library_app.controller;

import lombok.AllArgsConstructor;
import org.library.library_app.service.BookReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class BookReservationController {
    private final BookReservationService service;

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{bookId}/reserve")
    public ResponseEntity<Void> reserveBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails user) {
        service.reserveBook(bookId, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{bookId}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails user) {
        service.cancelReservation(bookId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{bookId}/extend")
    public ResponseEntity<Void> extendReservation(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails user) {
        service.extendReservation(bookId, user.getUsername());
        return ResponseEntity.ok().build();
    }
}
