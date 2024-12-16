package org.library.library_app.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookStatusTest {

    @Test
    void testToString() {
        assertEquals("Available", BookStatus.AVAILABLE.toString());
        assertEquals("Reserved", BookStatus.RESERVED.toString());
        assertEquals("Borrowed", BookStatus.BORROWED.toString());
        assertEquals("Overdue", BookStatus.OVERDUE.toString());
        assertEquals("Lost", BookStatus.LOST.toString());
    }

    @Test
    void fromString_ValidRequest_ShouldReturnBookStatus() {
        assertEquals(BookStatus.AVAILABLE, BookStatus.fromString("Available"));
        assertEquals(BookStatus.AVAILABLE, BookStatus.fromString("available"));
        assertEquals(BookStatus.AVAILABLE, BookStatus.fromString("AVAILABLE"));
    }
}