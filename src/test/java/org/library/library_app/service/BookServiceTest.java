package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.BookCategory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService service;

    @BeforeEach
    void resetMocks() {
        reset(authorRepository);
        reset(bookRepository);
    }
    @Test
    void addBook_WhenAuthorFound_ShouldReturnSavedBookDto() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        BookDto bookDto = new BookDto(null, "Title", List.of(1L), "Fiction", "Description", "Available");
        BookDto bookDtoWithId = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "Available");
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        BookDto result = service.addBook(bookDto);

        assertEquals(bookDtoWithId, result);

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
    }

    @Test
    void addBook_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        BookDto bookDto = new BookDto(null, "Title", List.of(1L), "Fiction", "Description", "Available");

        assertThrows(IllegalArgumentException.class, () -> service.addBook(bookDto));

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).saveAndFlush(any(Book.class));
    }

    @Test
    void getAllBooksDto_WhenNoBookFound_ShouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<BookDto> result = service.getAllBooksDto();

        assertEquals(List.of(), result);

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllBooksDto_WhenBooksFound_ShouldReturnListOfBooks() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> result = service.getAllBooksDto();

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "Available");
        assertEquals(List.of(bookDto), result);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookDto_WhenBookFound_ShouldReturnBookDto() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "Available");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<BookDto> result = service.getBookDto(1L);

        assertTrue(result.isPresent());
        assertEquals(bookDto, result.get());

        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void getBookDto_WhenBookNotFound_ShouldReturnEmptyOptional() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookDto> result = service.getBookDto(1L);

        assertTrue(result.isEmpty());

        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteBook_WhenBookFound_ShouldReturnTrue() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        boolean result = service.deleteBook(1L);

        assertTrue(result);

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldThrowIllegalArgumentException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.deleteBook(1L));

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateBook_WhenBookFound_ShouldReturnTrue() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "available");

        boolean result = service.updateBook(1L, bookDto);

        assertTrue(result);

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowIllegalArgumentException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "available");

        assertThrows(IllegalArgumentException.class, () -> service.updateBook(1L, bookDto));

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, never()).findById(anyLong());
    }

    @Test
    void updateBook_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "available");
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));


        assertThrows(IllegalArgumentException.class, () -> service.updateBook(1L, bookDto));

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

}