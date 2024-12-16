package org.library.library_app.tools;

import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoMapper {
    public static AuthorDto authorToDto(Author author) {
        Set<Long> booksIds = author.getBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
        return new AuthorDto(author.getId(), author.getFirstName(), author.getLastName(), booksIds);
    }


    public static BookDto bookToDto(Book book) {
        List<Long> authorsIds = book.getAuthors().stream()
                .map(Author::getId)
                .toList();
        BookDto bookData = new BookDto(book.getId(), book.getTitle(), authorsIds, book.getCategory().toString(), book.getDescription(), book.getStatus().toString());
        return bookData;
    }
}
