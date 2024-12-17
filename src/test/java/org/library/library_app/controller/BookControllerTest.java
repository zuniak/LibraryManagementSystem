package org.library.library_app.controller;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.exceptions.BookValidationException;
import org.library.library_app.service.BookService;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookControllerTest {
    @MockitoBean
    BookService service;

    @MockitoSpyBean
    Validator validator;

    @Autowired
    BookController controller;

    @BeforeEach
    void resetMocks() {
        reset(service);
        reset(validator);
    }

    @Test
    void addBook_WhenValidRequest_ShouldReturnCreated() {
        BookDto book = new BookDto(null, "Title", List.of(1L),
                "Fictional", "Description", "available");

        BookDto bookWithId = new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");

        when(service.addBook(book)).thenReturn(bookWithId);

        ResponseEntity<BookDto> response = controller.addBook(book);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookWithId, response.getBody());

        verify(validator, times(1)).validate(book, CreateBook.class);
        verify(service, times(1)).addBook(book);
    }

    @Test
    void addBook_WhenInvalidRequest_ShouldThrowBookValidationException() {
        BookDto book = new BookDto(null, "", List.of(),
                "", "", null);

        when(validator.validate(book, CreateBook.class)).thenThrow(new BookValidationException("Book create validation failed"));

        BookValidationException exception = assertThrows(BookValidationException.class,
                () -> controller.addBook(book));

        assertEquals("Book create validation failed", exception.getMessage());

        verify(validator, times(1)).validate(book, CreateBook.class);
        verify(service, never()).addBook(book);
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
        BookDto book = new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");

        when(service.getAllBooksDto()).thenReturn(List.of(book));

        ResponseEntity<List<BookDto>> response = controller.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(book), response.getBody());

        verify(service, times(1)).getAllBooksDto();
    }

    @Test
    void getBook_WhenBookFound_ShouldReturnOk() {
        BookDto book = new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");

        when(service.getBookDto(1L)).thenReturn(Optional.of(book));

        ResponseEntity<BookDto> response = controller.getBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());

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
        BookDto book = new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");

        ResponseEntity<Void> response = controller.updateBook(1L, book);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(validator, times(1)).validate(book, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenInvalidRequest_ShouldThrowBookValidationException(){
        BookDto book = new BookDto(null, "", List.of(),
                "", "", null);

        when(validator.validate(book, UpdateBook.class)).thenThrow(new BookValidationException("Book update validation failed"));

        BookValidationException exception = assertThrows(BookValidationException.class,
                () -> controller.updateBook(1L, book));

        assertEquals("Book update validation failed", exception.getMessage());

        verify(validator, times(1)).validate(book, UpdateBook.class);
        verify(service, never()).updateBook(anyLong(), any(BookDto.class));
    }

    @Test
    void updateBook_WhenValidRequestButBookNotFound_ShouldThrowBookNotFoundException(){
        BookDto book = new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");

        doThrow(new BookNotFoundException("Book with id 1 not found")).when(service).updateBook(1L, book);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> controller.updateBook(1L, book));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(book, UpdateBook.class);
        verify(service, times(1)).updateBook(anyLong(), any(BookDto.class));
    }
}