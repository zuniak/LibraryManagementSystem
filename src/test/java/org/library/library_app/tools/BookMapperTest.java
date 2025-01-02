package org.library.library_app.tools;

import org.junit.jupiter.api.Test;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {
    BookMapper bookMapper = Mappers.getMapper( BookMapper.class );

    @Test
    void bookToDto() {
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));

        BookDto result = bookMapper.bookToDto(book);

        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(List.of(author.getId()), result.getAuthorsIds());
        assertEquals(book.getCategory(), result.getCategory());
        assertEquals(book.getDescription(), result.getDescription());
        assertEquals(book.getStatus(), result.getStatus());
    }

    @Test
    void bookFromDto() {
        BookDto bookDto = BookMother.createDto(null, List.of(1L));

        Book result = bookMapper.bookFromDto(bookDto);

        assertNull(result.getId()); // The id is not set
        assertNull(result.getAuthors()); // The authors are not set

        assertEquals(bookDto.getTitle(), result.getTitle());
        assertEquals(bookDto.getCategory(), result.getCategory());
        assertEquals(bookDto.getDescription(), result.getDescription());
        assertEquals(bookDto.getStatus(), result.getStatus());
    }

    @Test
    void updateBookFromDto() {
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));

        BookDto bookDto = BookMother.createDtoValidUpdateBook(2L, List.of(2L));

        bookMapper.updateBookFromDto(book, bookDto);

        assertNotEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getCategory(), book.getCategory());
        assertEquals(bookDto.getDescription(), book.getDescription());
        assertEquals(bookDto.getStatus(), book.getStatus());

        assertNotEquals(bookDto.getAuthorsIds(), book.getAuthors().stream().map(Author::getId).toList()); // The authors are not updated
    }
}