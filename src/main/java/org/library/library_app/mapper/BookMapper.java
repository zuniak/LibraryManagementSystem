package org.library.library_app.mapper;

import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Book;

public class BookMapper {
    public static BookDto toDto(Book book) {
        return new BookDto(book.getTitle(), book.getAuthor());
    }

    public static Book toEntity(BookDto bookDto) {
        return new Book(bookDto.getTitle(), bookDto.getAuthor());
    }
}
