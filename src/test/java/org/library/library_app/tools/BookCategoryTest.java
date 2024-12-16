package org.library.library_app.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookCategoryTest {

    @Test
    void fromString_ValidRequest_ShouldReturnBookCategory() {
        assertEquals(BookCategory.FICTION, BookCategory.fromString("Fiction"));
        assertEquals(BookCategory.FICTION, BookCategory.fromString("fiction"));
        assertEquals(BookCategory.FICTION, BookCategory.fromString("FICTION"));
    }

    @Test
    void fromString_InvalidRequest_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BookCategory.fromString("xxxxx"));
    }

    @Test
    void testToString() {
        assertEquals("Fiction", BookCategory.FICTION.toString());
        assertEquals("Science", BookCategory.SCIENCE.toString());
        assertEquals("Art", BookCategory.ART.toString());
        assertEquals("History", BookCategory.HISTORY.toString());
        assertEquals("Biography", BookCategory.BIOGRAPHY.toString());
    }
}