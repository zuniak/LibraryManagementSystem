package org.library.library_app.exceptions;

public class AuthorIdDoNotMatchException extends RuntimeException{
    public AuthorIdDoNotMatchException() {
        super("Author id does not match");
    }
}
