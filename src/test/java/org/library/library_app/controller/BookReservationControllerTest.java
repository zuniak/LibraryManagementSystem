package org.library.library_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.service.BookReservationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReservationControllerTest {
    @Mock
    BookReservationService service;

    @InjectMocks
    BookReservationController controller;

    @BeforeEach
    void resetMocks(){
        reset(service);
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void reserveBook_whenValidRequest_shouldReturnOk() {
        LibraryUser user = new LibraryUser();
        user.setUsername("username");

        ResponseEntity<Void> response = controller.reserveBook(1L, user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(service, times(1)).reserveBook(1L, "username");
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void cancelReservation_whenValidRequest_shouldReturnOk() {
        LibraryUser user = new LibraryUser();
        user.setUsername("username");
        
        ResponseEntity<Void> response = controller.cancelReservation(1L, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).cancelReservation(1L, "username");
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    void extendReservation_whenValidRequest_shouldReturnOk() {
        LibraryUser user = new LibraryUser();
        user.setUsername("username");

        ResponseEntity<Void> response = controller.extendReservation(1L, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).extendReservation(1L, "username");
    }
}