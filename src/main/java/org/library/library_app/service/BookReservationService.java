package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.entity.Book;
import org.library.library_app.entity.BookReservation;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.exceptions.BookLimitExceededException;
import org.library.library_app.exceptions.BookReservationNotFoundException;
import org.library.library_app.exceptions.DueDateNotExtendedException;
import org.library.library_app.exceptions.DuplicateReservationException;
import org.library.library_app.repository.BookReservationRepository;
import org.library.library_app.tools.BookStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BookReservationService {
    public static final int DEFAULT_RESERVATION_DURATION = 7;
    public static final int MAX_RESERVATIONS = 5;
    public static final int EXTEND_PERIOD = 3;

    private final BookReservationRepository reservationRepository;

    private BookService bookService;
    private LibraryUserService userService;

    public void reserveBook(Long bookId, String username) {
        LibraryUser member = userService.getUserByUsername(username);
        Book book = bookService.getBook(bookId);
        canReserveBook(book, member);

        BookReservation bookReservation = new BookReservation(book, member);
        reservationRepository.save(bookReservation);

        activeReservation(bookReservation.getBook());
    }

    private void canReserveBook(Book book, LibraryUser member) {
        if (reservationRepository.countBookReservationsByMember(member) >= MAX_RESERVATIONS) {
            throw new BookLimitExceededException("Book reservation limit exceeded");
        }
        if (reservationRepository.findByBookAndMember(book, member).isPresent()){
            throw new DuplicateReservationException(member.getUsername(), book.getId());
        }
    }

    private BookReservation getReservation(Long bookId, String username) {
        LibraryUser member = userService.getUserByUsername(username);
        Book book = bookService.getBook(bookId);

        Optional<BookReservation> reservationOpt = reservationRepository.findByBookAndMember(book, member);
        if (reservationOpt.isEmpty()) {
            throw new BookReservationNotFoundException(book.getId(), member.getUsername());
        }
        return reservationOpt.get();
    }


    public void activeReservation(Book book) {
        Optional<BookReservation> bookReservationOpt = reservationRepository.findTopByBookOrderByStartDate(book);
        if (bookReservationOpt.isPresent() && book.getStatus().equals(BookStatus.AVAILABLE)) {
            book.setStatus(BookStatus.RESERVED);

            BookReservation bookReservation = bookReservationOpt.get();
            bookReservation.setReservationDueDate(getNewDate(new Date()));
        }
    }

    public void cancelReservation(Long bookId, String username) {
        BookReservation reservation = getReservation(bookId, username);
        Book book = reservation.getBook();
        if (isFirstReservation(reservation)) {
            book.setStatus(BookStatus.AVAILABLE);
        }

        reservationRepository.delete(reservation);
        activeReservation(book);
    }

    private boolean isFirstReservation(BookReservation reservation) {
        Optional<BookReservation> firstReservationOpt = reservationRepository.findTopByBookOrderByStartDate(reservation.getBook());
        return firstReservationOpt.isPresent() && reservation.getId().equals(firstReservationOpt.get().getId());
    }

    public void extendReservation(Long bookId, String username) {
        BookReservation reservation = getReservation(bookId, username);
        canExtend(reservation);
        reservation.setReservationDueDate(getNewDate(reservation.getReservationDueDate()));
    }

    private void canExtend(BookReservation reservation){
        if (!isFirstReservation(reservation)){
            throw new DueDateNotExtendedException("Reservation with id " + reservation.getId() + " is not active");
        }
        if (reservationRepository.countBookReservationsByBook(reservation.getBook()) > 1){
            throw new DueDateNotExtendedException("Reservation queue for book with id " + reservation.getBook().getId() + " is not empty");
        }
        if (tooSoon(reservation.getReservationDueDate())){
            throw new DueDateNotExtendedException("Cannot extend reservation because it is too soon");
        }
    }

    private Date getNewDate(Date start){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        calendar.add(Calendar.DAY_OF_MONTH, DEFAULT_RESERVATION_DURATION);

        return calendar.getTime();
    }

    private boolean tooSoon(Date dueDate) {
        Date now = new Date();
        long difference = dueDate.getTime() - now.getTime();
        return difference > EXTEND_PERIOD * 24 * 60 * 60 * 1000;
    }
}
