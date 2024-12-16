package org.library.library_app.entity;

import org.junit.jupiter.api.Test;
import org.library.library_app.tools.BookCategory;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void addAuthor() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);

        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);

        book.addAuthor(author);

        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains(author));
    }

    @Test
    void removeAuthor() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);

        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.setAuthors(new LinkedList<>(List.of(author)));

        book.removeAuthor(author);

        assertEquals(0, book.getAuthors().size());
        assertFalse(book.getAuthors().contains(author));
    }
}