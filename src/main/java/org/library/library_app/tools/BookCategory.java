package org.library.library_app.tools;

import org.library.library_app.exceptions.UnknownBookCategoryException;

public enum BookCategory {
    FICTION,
    SCIENCE,
    ART,
    HISTORY,
    BIOGRAPHY;

    public static BookCategory fromString(String category) {
        for (BookCategory bookCategory : BookCategory.values()) {
            if(bookCategory.name().equalsIgnoreCase(category)) {
                return bookCategory;
            }
        }
        throw new UnknownBookCategoryException("Unknown category: " + category);
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
