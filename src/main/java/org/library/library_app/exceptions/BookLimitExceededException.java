package org.library.library_app.exceptions;

public class BookLimitExceededException extends RuntimeException {
    public BookLimitExceededException(String message) {
        super(message);
    }
}
