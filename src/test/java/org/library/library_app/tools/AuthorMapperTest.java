package org.library.library_app.tools;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthorMapperTest {
    @Autowired
    AuthorMapper authorMapper;

    static Author author;

    static AuthorDto authorDto;


    @BeforeAll
    static void setUp() {
        author = new Author("Name", "LastName");
        author.setId(1L);

        Book book1 = new Book("Title", BookCategory.FICTION, "Description");
        book1.setId(1L);
        book1.addAuthor(author);
        author.addBook(book1);

        Book book2 = new Book("Title2", BookCategory.FICTION, "Description2");
        book2.setId(2L);
        book2.addAuthor(author);
        author.addBook(book2);

        authorDto = new AuthorDto(1L, "Name", "LastName", Set.of(1L, 2L));
    }

    @Test
    void authorToDto() {
        AuthorDto result = authorMapper.authorToDto(author);

        assertEquals(result.getId(), authorDto.getId());
        assertEquals(result.getFirstName(), authorDto.getFirstName());
        assertEquals(result.getLastName(), authorDto.getLastName());
        assertEquals(result.getBooksIds(), authorDto.getBooksIds());
    }
}