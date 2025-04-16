package org.library.library_app.exceptions;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super("Library user " + username + " already exists");
    }
}
