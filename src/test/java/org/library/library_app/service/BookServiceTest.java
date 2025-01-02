package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookIdDoNotMatchException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.library.library_app.tools.BookMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService service;

    @BeforeEach
    void resetMocks() {
        reset(authorRepository);
        reset(bookRepository);
    }

    @Test
    void addBook_WhenAuthorFound_ShouldReturnSavedBookDto() {
        Author author = AuthorMother.createAuthor(1L);

        BookDto bookDtoToSave = BookMother.createDtoValidCreateBook(List.of(1L));
        Book book = BookMother.createBook(null, List.of());
        BookDto savedBookDto = BookMother.createDto(1L, List.of(1L));

        when(bookMapper.bookFromDto(bookDtoToSave)).thenReturn(book);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        doAnswer(invocation -> {
            Book targetBook = invocation.getArgument(0);
            targetBook.setId(1L);
            return targetBook;
        }).when(bookRepository).saveAndFlush(book);
        when(bookMapper.bookToDto(book)).thenReturn(savedBookDto);

        BookDto result = service.addBook(bookDtoToSave);

        assertEquals(savedBookDto, result);
        assertEquals(Set.of(book), author.getBooks());
        assertEquals(List.of(author), book.getAuthors());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
        verify(bookMapper, times(1)).bookFromDto(any(BookDto.class));
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void addBook_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        BookDto bookDtoToSave = BookMother.createDtoValidCreateBook(List.of(1L));
        Book book = BookMother.createBook(null, List.of());

        when(bookMapper.bookFromDto(bookDtoToSave)).thenReturn(book);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.addBook(bookDtoToSave));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).saveAndFlush(any(Book.class));
        verify(bookMapper, times(1)).bookFromDto(any(BookDto.class));
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
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(bookMapper.bookToDto(book)).thenReturn(bookDto);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> result = service.getAllBooksDto();

        assertEquals(List.of(bookDto), result);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void getBookDto_WhenBookFound_ShouldReturnBookDto() {
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(bookMapper.bookToDto(book)).thenReturn(bookDto);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<BookDto> result = service.getBookDto(1L);

        assertTrue(result.isPresent());
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
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        service.deleteBook(1L);

        assertTrue(author.getBooks().isEmpty());

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
        Author author1 = AuthorMother.createAuthor(1L);
        Author author2 = AuthorMother.createAuthor(2L);

        Book book = BookMother.createBook(1L, List.of(author1));

        BookDto updatedBookDto = BookMother.createDtoValidUpdateBook(1L, List.of(2L));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2));
        when(bookRepository.save(book)).thenReturn(book);

        service.updateBook(1L, updatedBookDto);

        assertEquals(List.of(author2), book.getAuthors());

        assertTrue(author1.getBooks().isEmpty());
        assertEquals(Set.of(book), author2.getBooks());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookDto bookDto = BookMother.createDtoValidUpdateBook(1L, List.of(1L));

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> service.updateBook(1L, bookDto));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, never()).findById(anyLong());
    }

    @Test
    void updateBook_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));
        BookDto updatedBook = BookMother.createDtoValidUpdateBook(1L, List.of(2L));

        when(authorRepository.findById(2L)).thenReturn(Optional.empty());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.updateBook(1L, updatedBook));

        assertEquals("Author with id 2 not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBook_WhenBookIdDoesNotMatch_ShouldThrowBookIdDoNotMatchException() {
        Book book = BookMother.createBook(1L, List.of(AuthorMother.createAuthor(1L)));
        BookDto updatedBook = BookMother.createDtoValidUpdateBook(2L, List.of(1L));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookIdDoNotMatchException exception = assertThrows(BookIdDoNotMatchException.class,
                () -> service.updateBook(1L, updatedBook));

        assertEquals("Book id does not match", exception.getMessage());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
        verify(authorRepository, never()).findById(anyLong());
    }
}