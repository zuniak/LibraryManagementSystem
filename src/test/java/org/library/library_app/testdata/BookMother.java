package org.library.library_app.testdata;

import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.tools.BookCategory;
import org.library.library_app.tools.BookStatus;

import java.util.List;

public class BookMother {

    private final static String TITLE = "Title";
    private final static BookCategory CATEGORY = BookCategory.FICTION;
    private final static String DESCRIPTION = "Description";
    private final static BookStatus STATUS = BookStatus.AVAILABLE;


    public static Book createBook(Long bookId, List<Author> authors) {
        Book book = new Book(TITLE, CATEGORY, DESCRIPTION);
        book.setId(bookId);
        for (Author author : authors) {
            book.addAuthor(author);
            author.addBook(book);
        }
        return book;
    }

    public static BookDto createDto(Long bookId, List<Long> authorsIds) {
        return new BookDto(bookId, TITLE, authorsIds, CATEGORY, DESCRIPTION, STATUS);
    }

    public static BookDto createDtoValidCreateBook(List<Long> authorsIds) {
        return new BookDto(null, TITLE, authorsIds, CATEGORY, DESCRIPTION, null);
    }

    public static BookDto createDtoInvalidCreateBook() {
        return new BookDto(1L, "", List.of(), null, "", STATUS);
    }

    public static BookDto createDtoValidUpdateBook(Long bookId, List<Long> authorsIds) {
        return new BookDto(bookId, TITLE, authorsIds, CATEGORY, DESCRIPTION, STATUS);
    }

    public static BookDto createDtoInvalidUpdateBook() {
        return new BookDto( null, "", List.of(), null, "", null);
    }
}
