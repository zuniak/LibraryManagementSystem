package org.library.library_app.tools;

import org.junit.jupiter.api.Test;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.entity.Author;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthorMapperTest {
    
    AuthorMapper authorMapper = Mappers.getMapper( AuthorMapper.class );

    @Test
    void authorToDto() {
        Author author = AuthorMother.createAuthor(1L);
        BookMother.createBook(1L, List.of(author));

        AuthorDto result = authorMapper.authorToDto(author);

        assertEquals(1L, result.getId());
        assertEquals(author.getFirstName(), result.getFirstName());
        assertEquals(author.getLastName(), result.getLastName());
        assertEquals(Set.of(1L), result.getBooksIds());
    }

    @Test
    void authorFromDto() {
        AuthorDto authorDto = AuthorMother.createDtoValidCreateAuthor();

        Author result = authorMapper.authorFromDto(authorDto);

        assertNull(result.getId());
        assertEquals(authorDto.getFirstName(), result.getFirstName());
        assertEquals(authorDto.getLastName(), result.getLastName());
        assertEquals(0, result.getBooks().size());
    }

    @Test
    void updateAuthorFromDto() {
        Author author = AuthorMother.createAuthor(1L);

        AuthorDto updatedDto = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        authorMapper.updateAuthorFromDto(author, updatedDto);

        assertEquals(updatedDto.getFirstName(), author.getFirstName());
        assertEquals(updatedDto.getLastName(), author.getLastName());
    }
}