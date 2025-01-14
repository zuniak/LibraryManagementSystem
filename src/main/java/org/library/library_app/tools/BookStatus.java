package org.library.library_app.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.library.library_app.exceptions.UnknownBookStatusException;

public enum BookStatus {
    AVAILABLE,
    RESERVED,
    BORROWED,
    OVERDUE,
    LOST;

    @Override
    @JsonValue
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    @JsonCreator
    public static BookStatus fromString(String status) {
        for (BookStatus bookStatus : BookStatus.values()) {
            if (bookStatus.name().equalsIgnoreCase(status)) {
                return bookStatus;
            }
        }
        throw new UnknownBookStatusException("Unknown status: " + status);
    }
}
