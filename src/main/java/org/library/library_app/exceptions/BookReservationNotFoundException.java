package org.library.library_app.exceptions;

public class BookReservationNotFoundException extends RuntimeException {
    public BookReservationNotFoundException(Long bookId, String username) {
        super("No reservation found for book with id " + bookId + " by user " + username);
    }

    public BookReservationNotFoundException(Long bookId) {
        super("No reservation found for book with id " + bookId);
    }
}
