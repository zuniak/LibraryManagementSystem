package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.exceptions.AuthorIdDoNotMatchException;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.library.library_app.tools.AuthorMapper;
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
class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    AuthorMapper authorMapper;
    @Mock
    BookMapper bookMapper;

    @InjectMocks
    AuthorService service;

    @BeforeEach
    void resetMocks() {
        reset(authorRepository);
        reset(bookRepository);
        reset(authorMapper);
        reset(bookMapper);
    }

    @Test
    void addAuthor_ShouldReturnSavedAuthorDto() {
        AuthorDto authorDtoToSave = AuthorMother.createDtoValidCreateAuthor();
        Author author = AuthorMother.createAuthor(null);
        AuthorDto savedAuthorDto = AuthorMother.createDto(1L);

        when(authorMapper.authorFromDto(authorDtoToSave)).thenReturn(author);
        doAnswer(invocation -> {
            Author targetAuthor = invocation.getArgument(0);
            targetAuthor.setId(1L);
            return targetAuthor;
        }).when(authorRepository).saveAndFlush(author);
        when(authorMapper.authorToDto(author)).thenReturn(savedAuthorDto);

        AuthorDto result = service.addAuthor(authorDtoToSave);

        assertEquals(savedAuthorDto, result);

        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
        verify(authorMapper, times(1)).authorToDto(any(Author.class));
        verify(authorMapper, times(1)).authorFromDto(any(AuthorDto.class));
    }

    @Test
    void getAllAuthorsDto_WhenNoAuthorsFound_ShouldReturnEmptyList() {
        when(authorRepository.findAll()).thenReturn(List.of());

        List<AuthorDto> result = service.getAllAuthorsDto();

        assertEquals(List.of(), result);

        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, never()).authorToDto(any(Author.class));
    }

    @Test
    void getAllAuthorsDto_WhenAuthorFound_ShouldReturnListWithAuthor() {
        Author author = AuthorMother.createAuthor(1L);
        AuthorDto authorDto = AuthorMother.createDto(1L);

        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.authorToDto(author)).thenReturn(authorDto);

        List<AuthorDto> result = service.getAllAuthorsDto();

        assertEquals(List.of(authorDto), result);

        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).authorToDto(any(Author.class));
    }

    @Test
    void getAuthorDtoById_WhenAuthorNotFound_ShouldReturnEmptyOptional() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AuthorDto> result = service.getAuthorDtoById(1L);

        assertTrue(result.isEmpty());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorMapper, never()).authorToDto(any(Author.class));
    }

    @Test
    void getAuthorDtoById_WhenAuthorFound_ShouldReturnAuthorDto() {
        Author author = AuthorMother.createAuthor(1L);
        AuthorDto authorDto = AuthorMother.createDto(1L);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(authorMapper.authorToDto(author)).thenReturn(authorDto);

        Optional<AuthorDto> result = service.getAuthorDtoById(1L);

        assertTrue(result.isPresent());
        assertEquals(authorDto, result.get());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorMapper, times(1)).authorToDto(any(Author.class));
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorFoundWithBooks_ShouldReturnListOfBookDto() {
        Author author = AuthorMother.createAuthor(1L);
        Book book = BookMother.createBook(1L, List.of(author));
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookMapper.bookToDto(book)).thenReturn(bookDto);

        List<BookDto> result = service.getAuthorBooksDto(1L);

        assertEquals(List.of(bookDto), result);

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookMapper, times(1)).bookToDto(any(Book.class));
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorFoundWithNoBooks_ShouldReturnEmptyList() {
        Author author = AuthorMother.createAuthor(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        List<BookDto> result = service.getAuthorBooksDto(1L);

        assertEquals(List.of(), result);

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookMapper, never()).bookToDto(any(Book.class));
    }

    @Test
    void getAuthorsBooksDto_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.getAuthorBooksDto(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor_WhenAuthorFound_ShouldReturnTrue() {
        Author author = AuthorMother.createAuthor(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        service.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteAuthor_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.deleteAuthor(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, never()).deleteById(anyLong());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateAuthor_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> service.updateAuthor(1L, updatedAuthor));


        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(authorRepository, never()).save(any(Author.class));
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateAuthor_WhenAuthorFoundButNewBookNotFound_ShouldThrowBookNotFoundException() {
        Author author = AuthorMother.createAuthor(1L);

        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> service.updateAuthor(1L, updatedAuthor));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateAuthor_WhenAuthorFoundAndNewBooksFound_ShouldUpdateAuthorBooks() {
        Author author = AuthorMother.createAuthor(1L);

        Book book1 = BookMother.createBook(1L, List.of(author));
        Book book2 = BookMother.createBook(2L, List.of(author));

        AuthorDto updatedAuthorDto = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(2L, 3L));

        Book book3 = BookMother.createBook(3L, List.of(AuthorMother.createAuthor(2L)));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        doNothing().when(authorMapper).updateAuthorFromDto(author, updatedAuthorDto);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book3));

        service.updateAuthor(1L, updatedAuthorDto);

        assertEquals(Set.of(book2, book3), author.getBooks());
        assertEquals(2, book3.getAuthors().size());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(2)).findById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void updateAuthor_WhenAuthorIdDoesNotMatch_ShouldThrowAuthorIdDoNotMatchException() {
        Author author = AuthorMother.createAuthor(1L);

        AuthorDto updatedAuthorDto = AuthorMother.createDtoValidUpdateAuthor(2L, Set.of(1L));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorIdDoNotMatchException exception = assertThrows(AuthorIdDoNotMatchException.class,
                () -> service.updateAuthor(1L, updatedAuthorDto));

        assertEquals("Author id does not match", exception.getMessage());

        verify(authorRepository, times(1)).findById(anyLong());
    }
}