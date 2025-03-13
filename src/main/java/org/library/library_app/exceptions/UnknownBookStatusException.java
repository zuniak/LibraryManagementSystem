package org.library.library_app.exceptions;

public class UnknownBookStatusException extends RuntimeException {
    public UnknownBookStatusException(String message) {
        super(message);
    }
}
