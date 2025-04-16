package org.library.library_app.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long authorId) {
        super("Author with id " + authorId + " not found");
    }
}
