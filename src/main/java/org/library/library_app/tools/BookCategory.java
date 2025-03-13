package org.library.library_app.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.library.library_app.exceptions.UnknownBookCategoryException;

public enum BookCategory {
    FICTION,
    SCIENCE,
    ART,
    HISTORY,
    BIOGRAPHY;

    @JsonCreator
    public static BookCategory fromString(String category) {
        for (BookCategory bookCategory : BookCategory.values()) {
            if(bookCategory.name().equalsIgnoreCase(category)) {
                return bookCategory;
            }
        }
        throw new UnknownBookCategoryException("Unknown category: " + category);
    }

    @Override
    @JsonValue
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
