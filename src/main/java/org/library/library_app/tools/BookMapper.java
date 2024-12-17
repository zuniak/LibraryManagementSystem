package org.library.library_app.tools;

import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "authors", target = "authorsIds")
    BookDto bookToDto(Book book);

    default Long mapAuthorToId(Author author) {
        return author.getId();
    }
}