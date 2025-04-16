package org.library.library_app.exceptions;

public class BookIdDoNotMatchException extends RuntimeException{
    public BookIdDoNotMatchException() {
        super("Book id does not match");
    }
}
