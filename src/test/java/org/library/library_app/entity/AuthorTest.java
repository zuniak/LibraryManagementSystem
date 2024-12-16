package org.library.library_app.entity;

import org.junit.jupiter.api.Test;
import org.library.library_app.tools.BookCategory;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    @Test
    void addBook() {
        Author author = new Author("First Name", "Last Name");

        Book book = new Book("Title", BookCategory.FICTION, "Description");

        author.addBook(book);

        assertEquals(1, author.getBooks().size());
        assertTrue(author.getBooks().contains(book));
    }

    @Test
    void removeBook_WhenBookExist() {
        Book book = new Book("Title", BookCategory.FICTION, "Description");

        Author author = new Author("First Name", "Last Name");
        author.addBook(book);

        author.removeBook(book);

        assertEquals(0, author.getBooks().size());
        assertFalse(author.getBooks().contains(book));
    }
    @Test
    void removeBook_WhenBookDoNotExist() {
        Book book = new Book("Title", BookCategory.FICTION, "Description");

        Author author = new Author("First Name", "Last Name");

        author.removeBook(book);

        assertEquals(0, author.getBooks().size());
        assertFalse(author.getBooks().contains(book));
    }
}