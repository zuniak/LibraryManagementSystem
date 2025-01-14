package org.library.library_app.tools;

import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "authors", target = "authorsIds")
    BookDto bookToDto(Book book);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "authors")
    Book bookFromDto(BookDto bookDto);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "authors")
    void updateBookFromDto(@MappingTarget Book book, BookDto bookDto);

    default Long mapAuthorToId(Author author) {
        return author.getId();
    }
}