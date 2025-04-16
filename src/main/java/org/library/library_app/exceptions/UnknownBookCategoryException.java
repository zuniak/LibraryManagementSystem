package org.library.library_app.exceptions;

public class UnknownBookCategoryException extends RuntimeException {
    public UnknownBookCategoryException(String category) {
        super("Unknown category: " + category);
    }
}
