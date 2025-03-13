package org.library.library_app.testdata;

import org.library.library_app.dto.AuthorDto;
import org.library.library_app.entity.Author;

import java.util.HashSet;
import java.util.Set;

public class AuthorMother {

    private final static String FIRSTNAME = "Jane";
    private final static String LASTNAME = "Doe";


    public static Author createAuthor(Long authorId) {
        Author author = new Author(FIRSTNAME, LASTNAME);
        author.setId(authorId);
        return author;
    }

    public static AuthorDto createDto(Long authorId) {
        return new AuthorDto(authorId, FIRSTNAME, LASTNAME, new HashSet<>());
    }

    public static AuthorDto createDtoValidCreateAuthor() {
        return new AuthorDto(null, FIRSTNAME, LASTNAME, null);
    }

    public static AuthorDto createDtoInvalidCreateAuthor() {
        return new AuthorDto(1L, "", "", Set.of(1L));
    }

    public static AuthorDto createDtoValidUpdateAuthor(Long authorId, Set<Long> booksIds) {
        return new AuthorDto(authorId, "Updated Name", "Updated LastName", booksIds);
    }

    public static AuthorDto createDtoInvalidUpdateAuthor() {
        return new AuthorDto(null, "", "", null);
    }
}
