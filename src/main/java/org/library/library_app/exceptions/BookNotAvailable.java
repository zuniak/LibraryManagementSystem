package org.library.library_app.exceptions;

public class BookNotAvailable extends RuntimeException {
    public BookNotAvailable(Long bookId) {
        super("Book with id " + bookId + " is not available");
    }
}
