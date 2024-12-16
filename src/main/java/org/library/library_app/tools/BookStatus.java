package org.library.library_app.tools;

public enum BookStatus {
    AVAILABLE,
    RESERVED,
    BORROWED,
    OVERDUE,
    LOST;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static BookStatus fromString(String status) {
        for (BookStatus bookStatus : BookStatus.values()) {
            if (bookStatus.name().equalsIgnoreCase(status)) {
                return bookStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
