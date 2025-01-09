package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookIdDoNotMatchException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.exceptions.BookValidationException;
import org.library.library_app.service.BookService;
import org.library.library_app.testdata.BookMother;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    BookService service;

    @Mock
    Validator validator;

    @InjectMocks
    BookController controller;

    @BeforeEach
    void resetMocks() {
        reset(service);
        reset(validator);
    }

    @Test
    void addBook_WhenValidRequest_ShouldReturnCreated() {
        BookDto bookDto = BookMother.createDtoValidCreateBook(List.of(1L));
        BookDto savedBookDto = BookMother.createDto(1L, List.of(1L));

        when(validator.validate(bookDto, CreateBook.class)).thenReturn(Set.of());
        when(service.addBook(bookDto)).thenReturn(savedBookDto);

        ResponseEntity<BookDto> response = controller.addBook(bookDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBookDto, response.getBody());

        verify(validator, times(1)).validate(bookDto, CreateBook.class);
        verify(service, times(1)).addBook(bookDto);
    }

    @Test
    void addBook_WhenInvalidRequest_ShouldThrowBookValidationException() {
        BookDto bookDto = BookMother.createDtoInvalidCreateBook();

        @SuppressWarnings("unchecked") // Mock used to make validator returns not empty set.
        ConstraintViolation<BookDto> violation = mock(ConstraintViolation.class);

        when(validator.validate(bookDto, CreateBook.class)).thenReturn(Set.of(violation));

        BookValidationException exception = assertThrows(BookValidationException.class,
                () -> controller.addBook(bookDto));

        assertEquals("Book create validation failed", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, CreateBook.class);
        verify(service, never()).addBook(bookDto);
    }

    @Test
    void addBook_WhenValidRequestButAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        BookDto bookDto = BookMother.createDtoValidCreateBook(List.of(1L));

        when(validator.validate(bookDto, CreateBook.class)).thenReturn(Set.of());
        when(service.addBook(bookDto)).thenThrow(new AuthorNotFoundException("Author with id 1 not found"));

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.addBook(bookDto));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, CreateBook.class);
        verify(service, times(1)).addBook(bookDto);
    }



    @Test
    void getAllBooks_WhenNoBooksFound_ShouldReturnNoContent() {
        when(service.getAllBooksDto()).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getAllBooks();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAllBooksDto();
    }

    @Test
    void getAllBooks_WhenBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getAllBooksDto()).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getAllBooksDto();
    }

    @Test
    void getBooksThatArePartOfSeries_WhenNoBooksFound_ShouldReturnNoContent() {
        when(service.getBooksDtoThatArePartOfSeries()).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getBooksThatArePartOfSeries();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getBooksDtoThatArePartOfSeries();
    }

    @Test
    void getBooksThatArePartOfSeries_WhenBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getBooksDtoThatArePartOfSeries()).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getBooksThatArePartOfSeries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getBooksDtoThatArePartOfSeries();
    }

    @Test
    void getBooksBySeriesName_WhenNoBooksFound_ShouldReturnNoContent() {
        when(service.getBooksDtoBySeriesName("seriesName")).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getBooksBySeriesName("seriesName");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getBooksDtoBySeriesName("seriesName");
    }

    @Test
    void getBooksBySeriesName_WhenBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getBooksDtoBySeriesName("seriesName")).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getBooksBySeriesName("seriesName");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getBooksDtoBySeriesName("seriesName");
    }

    @Test
    void getBooksBySeriesNameAndNumber_WhenNoBooksFound_ShouldReturnNoContent() {
        when(service.getBooksBySeriesNameAndNumber("seriesName", 1)).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getBooksBySeriesNameAndNumber("seriesName", 1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getBooksBySeriesNameAndNumber("seriesName", 1);
    }

    @Test
    void getBooksBySeriesNameAndNumber_WhenBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getBooksBySeriesNameAndNumber("seriesName", 1)).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getBooksBySeriesNameAndNumber("seriesName", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getBooksBySeriesNameAndNumber("seriesName", 1);
    }

    @Test
    void getBook_WhenBookFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getBookDto(1L)).thenReturn(Optional.of(bookDto));

        ResponseEntity<BookDto> response = controller.getBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDto, response.getBody());

        verify(service, times(1)).getBookDto(anyLong());
    }

    @Test
    void getBook_WhenBookNotFound_ShouldReturnNotFound() {
        when(service.getBookDto(1L)).thenReturn(Optional.empty());

        ResponseEntity<BookDto> response = controller.getBook(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(service, times(1)).getBookDto(anyLong());
    }

    @Test
    void getBooksByTitle_WhenNoBooksFound_ShouldReturnNoContent() {
        when(service.getBooksDtoByTitle("title")).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getBooksByTitle("title");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getBooksDtoByTitle("title");
    }

    @Test
    void getBooksByTitle_WhenBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getBooksDtoByTitle("title")).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getBooksByTitle("title");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getBooksDtoByTitle("title");
    }

    @Test
    void deleteBook_WhenBookFound_ShouldReturnNoContent(){
        ResponseEntity<Void> response = controller.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).deleteBook(1L);
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldThrownBookNotFoundRequest(){
        doThrow(new BookNotFoundException("Book with id 1 not found")).when(service).deleteBook(1L);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> controller.deleteBook(1L));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(service, times(1)).deleteBook(1L);
    }

    @Test
    void updateBook_WhenValidRequestAndBookFound_ShouldReturnNoContent(){
        BookDto bookDto = BookMother.createDtoValidUpdateBook(1L, List.of(1L));

        when(validator.validate(bookDto, UpdateBook.class)).thenReturn(Set.of());

        ResponseEntity<Void> response = controller.updateBook(1L, bookDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(validator, times(1)).validate(bookDto, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenInvalidRequest_ShouldThrowBookValidationException(){
        BookDto bookDto = BookMother.createDtoInvalidUpdateBook();

        @SuppressWarnings("unchecked") // Mock used to make validator returns not empty set.
        ConstraintViolation<BookDto> violation = mock(ConstraintViolation.class);

        when(validator.validate(bookDto, UpdateBook.class)).thenReturn(Set.of(violation));

        BookValidationException exception = assertThrows(BookValidationException.class,
                () -> controller.updateBook(1L, bookDto));

        assertEquals("Book update validation failed", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, UpdateBook.class);
        verify(service, never()).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenValidRequestButBookNotFound_ShouldThrowBookNotFoundException(){
        BookDto bookDto = BookMother.createDtoValidUpdateBook(1L, List.of(1L));

        when(validator.validate(bookDto, UpdateBook.class)).thenReturn(Set.of());

        doThrow(new BookNotFoundException("Book with id 1 not found")).when(service).updateBook(1L, bookDto);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> controller.updateBook(1L, bookDto));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenValidRequestButBookNotFound_ShouldThrowAuthorNotFoundException(){
        BookDto bookDto = BookMother.createDtoValidUpdateBook(1L, List.of(1L));

        when(validator.validate(bookDto, UpdateBook.class)).thenReturn(Set.of());

        doThrow(new AuthorNotFoundException("Author with id 1 not found")).when(service).updateBook(1L, bookDto);

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.updateBook(1L, bookDto));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenValidRequestButBookNotFound_ShouldThrowBookIdDoNotMatchException(){
        BookDto bookDto = BookMother.createDtoValidUpdateBook(1L, List.of(1L));

        when(validator.validate(bookDto, UpdateBook.class)).thenReturn(Set.of());

        doThrow(new BookIdDoNotMatchException("Book id does not match")).when(service).updateBook(2L, bookDto);

        BookIdDoNotMatchException exception = assertThrows(BookIdDoNotMatchException.class,
                () -> controller.updateBook(2L, bookDto));

        assertEquals("Book id does not match", exception.getMessage());

        verify(validator, times(1)).validate(bookDto, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }
}