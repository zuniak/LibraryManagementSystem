package org.library.library_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.entity.Book;
import org.library.library_app.entity.BookReservation;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.exceptions.BookLimitExceededException;
import org.library.library_app.exceptions.BookReservationNotFoundException;
import org.library.library_app.exceptions.DueDateNotExtendedException;
import org.library.library_app.exceptions.DuplicateReservationException;
import org.library.library_app.repository.BookReservationRepository;
import org.library.library_app.tools.BookStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReservationServiceTest {
    @Mock
    BookReservationRepository reservationRepository;

    @Mock
    BookService bookService;

    @Mock
    LibraryUserService userService;

    @InjectMocks
    BookReservationService reservationService;

    @Test
    void reserveBook_shouldReserveBookAndActivateReservation(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();
        book.setStatus(BookStatus.AVAILABLE);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.countBookReservationsByMember(user)).thenReturn(0);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.empty());
        BookReservation bookReservation = new BookReservation(book, user);
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(bookReservation));

        reservationService.reserveBook(1L, "username");

        assertNotNull(bookReservation.getReservationDueDate());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).countBookReservationsByMember(user);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).save(any(BookReservation.class));
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
    }

    @Test
    void reserveBook_shouldReserveBookButShouldNotActivateReservation(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();
        book.setStatus(BookStatus.BORROWED);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.countBookReservationsByMember(user)).thenReturn(0);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.empty());
        BookReservation bookReservation = new BookReservation(book, user);
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(bookReservation));

        reservationService.reserveBook(1L, "username");

        assertNull(bookReservation.getReservationDueDate());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).countBookReservationsByMember(user);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).save(any(BookReservation.class));
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
    }

    @Test
    void reserveBook_whenReservationLimitExceeded_shouldThrowBookLimitExceededException(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.countBookReservationsByMember(user)).thenReturn(5);

        BookLimitExceededException exception = assertThrows(BookLimitExceededException.class, () -> reservationService.reserveBook(1L, "username"));
        assertEquals("Book reservation limit exceeded", exception.getMessage());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).countBookReservationsByMember(user);
        verify(reservationRepository, never()).save(any(BookReservation.class));
    }

    @Test
    void reserveBook_whenReservationRequestAlreadyMade_shouldThrowDuplicateReservationException(){
        LibraryUser user = new LibraryUser();
        user.setUsername("username");
        Book book = new Book();
        book.setId(1L);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.countBookReservationsByMember(user)).thenReturn(0);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(new BookReservation(book, user)));
        DuplicateReservationException exception = assertThrows(DuplicateReservationException.class, () -> reservationService.reserveBook(1L, "username"));
        assertEquals("User username already requested reservation for book with id 1", exception.getMessage());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).countBookReservationsByMember(user);
        verify(reservationRepository, never()).save(any(BookReservation.class));
    }

    @Test
    void cancelReservation_whenReservationExist_shouldRemoveReservation(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();
        book.setStatus(BookStatus.RESERVED);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        BookReservation bookReservation = new BookReservation(book, user);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(bookReservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(bookReservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.empty());

        reservationService.cancelReservation(1L, "username");

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).delete(bookReservation);
        verify(reservationRepository, times(2)).findTopByBookOrderByStartDate(book);
    }

    @Test
    void cancelReservation_whenReservationDoesNotExist_shouldThrowBookReservationNotFoundException(){
        LibraryUser user = new LibraryUser();
        user.setUsername("username");
        Book book = new Book();
        book.setId(1L);
        book.setStatus(BookStatus.RESERVED);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.empty());

        BookReservationNotFoundException exception = assertThrows(BookReservationNotFoundException.class, () -> reservationService.cancelReservation(1L, "username"));
        assertEquals("No reservation found for book with id 1 by user username", exception.getMessage());


        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, never()).delete(any(BookReservation.class));
    }

    @Test
    void extendReservation_shouldExtendReservation(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();

        BookReservation reservation = new BookReservation(book, user);
        reservation.setId(1L);
        Date oldDate = getNewDate(reservation.getStartDate(), 1);
        reservation.setReservationDueDate(oldDate);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(reservation));
        when(reservationRepository.countBookReservationsByBook(book)).thenReturn(1);

        reservationService.extendReservation(1L, "username");
        assertNotEquals(oldDate, reservation.getReservationDueDate());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
        verify(reservationRepository, times(1)).countBookReservationsByBook(book);
    }

    @Test
    void extendReservation_whenReservationNotActive_shouldThrowDueDateNotExtendedException(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();

        BookReservation reservation = new BookReservation(book, user);
        reservation.setId(1L);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(new BookReservation()));

        DueDateNotExtendedException exception = assertThrows(DueDateNotExtendedException.class, () -> reservationService.extendReservation(1L, "username"));
        assertEquals("Reservation with id 1 is not active", exception.getMessage());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
    }

    @Test
    void extendReservation_whenOtherReservation_shouldThrowDueDateNotExtendedException(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();
        book.setId(1L);

        BookReservation reservation = new BookReservation(book, user);
        reservation.setId(1L);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(reservation));
        when(reservationRepository.countBookReservationsByBook(book)).thenReturn(2);

        DueDateNotExtendedException exception = assertThrows(DueDateNotExtendedException.class, () -> reservationService.extendReservation(1L, "username"));
        assertEquals("Reservation queue for book with id 1 is not empty", exception.getMessage());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
        verify(reservationRepository, times(1)).countBookReservationsByBook(book);
    }

    @Test
    void extendReservation_whenIsTooSoon_shouldThrowDueDateNotExtendedException(){
        LibraryUser user = new LibraryUser();
        Book book = new Book();

        BookReservation reservation = new BookReservation(book, user);
        reservation.setId(1L);
        Date oldDate = getNewDate(reservation.getStartDate(), 7);
        reservation.setReservationDueDate(oldDate);

        when(userService.getUserByUsername("username")).thenReturn(user);
        when(bookService.getBook(1L)).thenReturn(book);
        when(reservationRepository.findByBookAndMember(book, user)).thenReturn(Optional.of(reservation));
        when(reservationRepository.findTopByBookOrderByStartDate(book)).thenReturn(Optional.of(reservation));
        when(reservationRepository.countBookReservationsByBook(book)).thenReturn(1);

        DueDateNotExtendedException exception = assertThrows(DueDateNotExtendedException.class, () -> reservationService.extendReservation(1L, "username"));
        assertEquals("Cannot extend reservation because it is too soon", exception.getMessage());

        verify(userService, times(1)).getUserByUsername("username");
        verify(bookService, times(1)).getBook(1L);
        verify(reservationRepository, times(1)).findByBookAndMember(book, user);
        verify(reservationRepository, times(1)).findTopByBookOrderByStartDate(book);
        verify(reservationRepository, times(1)).countBookReservationsByBook(book);
    }

    private Date getNewDate(Date start, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        calendar.add(Calendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }
}