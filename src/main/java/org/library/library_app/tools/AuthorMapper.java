package org.library.library_app.tools;

import org.library.library_app.dto.AuthorDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    @Mapping(source = "books", target = "booksIds")
    AuthorDto authorToDto(Author author);

    default Long mapBookToId(Book book) {
        return book.getId();
    }
}
