package org.library.library_app.exceptions;

public class DuplicateReservationException extends RuntimeException {
    public DuplicateReservationException(String username, Long bookId) {
        super("User " + username + " already requested reservation for book with id " + bookId);
    }
}
