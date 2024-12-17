package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.BookCategory;
import org.library.library_app.tools.BookMapper;
import org.library.library_app.tools.BookStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {
    @MockitoBean
    AuthorRepository authorRepository;
    @MockitoBean
    BookRepository bookRepository;

    @MockitoSpyBean
    BookMapper bookMapper;

    @Autowired
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

        BookDto bookDto = new BookDto(null, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
        BookDto bookDtoWithId = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        BookDto result = service.addBook(bookDto);

        assertEquals(bookDtoWithId, result);

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void addBook_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        BookDto bookDto = new BookDto(null, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.addBook(bookDto));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).saveAndFlush(any(Book.class));
        verify(bookMapper, never()).bookToDto(any(Book.class));

    }

    @Test
    void getAllBooksDto_WhenNoBookFound_ShouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<BookDto> result = service.getAllBooksDto();

        assertEquals(List.of(), result);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, never()).bookToDto(any(Book.class));
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

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
        assertEquals(List.of(bookDto), result);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void getBookDto_WhenBookFound_ShouldReturnBookDto() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<BookDto> result = service.getBookDto(1L);

        assertTrue(result.isPresent());

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
        assertEquals(bookDto, result.get());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void getBookDto_WhenBookNotFound_ShouldReturnEmptyOptional() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookDto> result = service.getBookDto(1L);

        assertTrue(result.isEmpty());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookMapper, never()).bookToDto(any(Book.class));
    }

    @Test
    void deleteBook_WhenBookFound_ShouldDelete() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        service.deleteBook(1L);

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> service.deleteBook(1L));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateBook_WhenBookFound_ShouldUpdateBook() {
        Author author1 = new Author("First Name1", "Last Name1");
        author1.setId(1L);
        Author author2 = new Author("First Name2", "Last Name2");
        author1.setId(2L);

        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2));
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author1);
        author1.addBook(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = new BookDto(1L, "Title2", List.of(2L), BookCategory.HISTORY, "Description2", BookStatus.RESERVED);

        service.updateBook(1L, bookDto);

        assertEquals("Title2", book.getTitle());
        assertEquals(List.of(author2), book.getAuthors());
        assertEquals(BookCategory.HISTORY, book.getCategory());
        assertEquals("Description2", book.getDescription());
        assertEquals(BookStatus.RESERVED, book.getStatus());

        assertTrue(author1.getBooks().isEmpty());
        assertTrue(author2.getBooks().contains(book));

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> service.updateBook(1L, bookDto));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, never()).findById(anyLong());
    }

    @Test
    void updateBook_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), BookCategory.FICTION, "Description", BookStatus.AVAILABLE);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.updateBook(1L, bookDto));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

}