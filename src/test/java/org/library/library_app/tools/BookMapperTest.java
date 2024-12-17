package org.library.library_app.tools;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookMapperTest {
    @Autowired
    BookMapper bookMapper;

    static Book book;

    static BookDto bookDto;



    @BeforeAll
    static void setUp() {
        Author author = new Author("Name", "LastName");
        author.setId(1L);

        book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);

        bookDto = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
    }

    @Test
    void bookToDto() {
        BookDto result = bookMapper.bookToDto(book);

        assertEquals(result.getId(), bookDto.getId());
        assertEquals(result.getTitle(), bookDto.getTitle());
        assertEquals(result.getAuthorsIds(), bookDto.getAuthorsIds());
        assertEquals(result.getCategory(), bookDto.getCategory());
        assertEquals(result.getDescription(), bookDto.getDescription());
        assertEquals(result.getStatus(), bookDto.getStatus());
    }
}