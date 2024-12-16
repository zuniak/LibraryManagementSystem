package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.BookCategory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    AuthorService service;

    @BeforeEach
    void resetMocks() {
        reset(authorRepository);
        reset(bookRepository);
    }

    @Test
    void addAuthor_ShouldReturnSavedAuthorDto() {
        AuthorDto authorDto = new AuthorDto(null, "First Name", "Last Name", null);
        AuthorDto authorDtoWithId = new AuthorDto(1L, "First Name", "Last Name", Set.of());
        Author authorWithId = new Author("First Name", "Last Name");
        authorWithId.setId(1L);

        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(authorWithId);

        AuthorDto outAuthor = service.addAuthor(authorDto);

        assertEquals(authorDtoWithId, outAuthor);

        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }

    @Test
    void getAllAuthorsDto_WhenNoAuthorsFound_ShouldReturnEmptyList() {
        when(authorRepository.findAll()).thenReturn(List.of());

        List<AuthorDto> result = service.getAllAuthorsDto();

        assertEquals(List.of(), result);

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void getAllAuthorsDto_WhenAuthorFound_ShouldReturnListWithAuthor() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        AuthorDto authorDto = new AuthorDto(1L, "First Name", "Last Name", Set.of());


        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<AuthorDto> result = service.getAllAuthorsDto();

        assertEquals(List.of(authorDto), result);

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void getAuthorDtoById_WhenAuthorNotFound_ShouldReturnEmptyOptional() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AuthorDto> result = service.getAuthorDtoById(1L);

        assertEquals(Optional.empty(), result);

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAuthorDtoById_WhenAuthorFound_ShouldReturnAuthorDto() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        AuthorDto authorDto = new AuthorDto(1L, "First Name", "Last Name", Set.of());

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Optional<AuthorDto> result = service.getAuthorDtoById(1L);

        assertTrue(result.isPresent());
        assertEquals(authorDto, result.get());

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorFoundWithBooks_ShouldReturnListOfBookDto() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        Book book = new Book("Title", BookCategory.FICTION, "Description");
        book.setId(1L);
        book.addAuthor(author);
        author.addBook(book);
        BookDto bookDto = new BookDto(1L, "Title", List.of(1L), "Fiction", "Description", "Available");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        List<BookDto> result = service.getAuthorBooksDto(1L);

        assertEquals(List.of(bookDto), result);

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorFoundWithNoBooks_ShouldReturnEmptyList() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        List<BookDto> result = service.getAuthorBooksDto(1L);

        assertEquals(List.of(), result);

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getAuthorBooksDto(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor_WhenAuthorFound_ShouldReturnTrue() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        boolean result = service.deleteAuthor(1L);

        assertTrue(result);

        verify(authorRepository, times(1)).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.deleteAuthor(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, never()).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateAuthor_WhenAuthorNotFound_ShouldThrowIllegalArgumentException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateAuthor(1L, new AuthorDto(1L, "First Name", "Last Name", null)));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, never()).save(any(Author.class));
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateAuthor_WhenAuthorFoundButNewBookNotFound_ShouldThrowIllegalArgumentException() {
        Author author = new Author("First Name", "Last Name");
        author.setId(1L);

        AuthorDto updatedAuthor = new AuthorDto(1L, "New First Name", "New Last Name", Set.of(1L));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateAuthor(1L, updatedAuthor));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateAuthor_WhenAuthorFoundAndNewBooksFound_ShouldUpdateAuthorBooks() {
        Book book1 = new Book("Title1", BookCategory.FICTION, "Description1");
        book1.setId(1L);
        Book book2 = new Book("Title2", BookCategory.FICTION, "Description2");
        book2.setId(2L);
        Book book3 = new Book("Title3", BookCategory.FICTION, "Description3");
        book3.setId(3L);


        Author author = new Author("First Name", "Last Name");
        author.setId(1L);
        author.addBook(book1);
        author.addBook(book2);
        book1.addAuthor(author);
        book2.addAuthor(author);

        AuthorDto updatedAuthor = new AuthorDto(1L, "New First Name", "New Last Name", Set.of(2L, 3L));


        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book3));

        boolean result = service.updateAuthor(1L, updatedAuthor);

        assertTrue(result);
        assertEquals(Set.of(book2, book3), author.getBooks());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(2)).findById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).save(any(Author.class));
    }
}