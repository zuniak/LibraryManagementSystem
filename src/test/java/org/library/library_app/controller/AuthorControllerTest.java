package org.library.library_app.controller;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.AuthorValidationException;
import org.library.library_app.service.AuthorService;
import org.library.library_app.validationgroups.CreateAuthor;
import org.library.library_app.validationgroups.UpdateAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorControllerTest {
    @MockitoBean
    AuthorService service;

    @MockitoSpyBean
    Validator validator;

    @Autowired
    AuthorController controller;

    @BeforeEach
    void resetMocks() {
        reset(service);
        reset(validator);
    }

    @Test
    void addAuthor_WhenValidRequest_ShouldReturnCreated() {
        AuthorDto author = new AuthorDto(null, "First Name", "Last Name", null);
        AuthorDto authorDtoWithId = new AuthorDto(1L, "First Name", "Last Name", null);

        when(service.addAuthor(author)).thenReturn(authorDtoWithId);

        ResponseEntity<AuthorDto> response = controller.addAuthor(author);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(authorDtoWithId, response.getBody());

        verify(validator, times(1)).validate(author, CreateAuthor.class);
        verify(service, times(1)).addAuthor(author);
    }

    @Test
    void addAuthor_WhenInvalidRequest_ShouldThrowAuthorValidationException() {
        AuthorDto author = new AuthorDto(null, "", "", null);

        when(validator.validate(author, CreateAuthor.class)).thenThrow(new AuthorValidationException("Author create validation failed"));

        AuthorValidationException exception = assertThrows(AuthorValidationException.class,
                () -> controller.addAuthor(author));

        assertEquals("Author create validation failed", exception.getMessage());

        verify(validator, times(1)).validate(author, CreateAuthor.class);
        verify(service, never()).addAuthor(author);
    }

    @Test
    void getAllAuthors_WhenNoAuthorsFound_ShouldReturnNoContent() {
        when(service.getAllAuthorsDto()).thenReturn(List.of());

        ResponseEntity<List<AuthorDto>> response = controller.getAllAuthors();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAllAuthorsDto();
    }

    @Test
    void getAllAuthors_WhenAuthorsFound_ShouldReturnOk() {
        AuthorDto author = new AuthorDto(1L, "Name", "Lastname", new HashSet<>(1));
        when(service.getAllAuthorsDto()).thenReturn(List.of(author));

        ResponseEntity<List<AuthorDto>> response = controller.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(author), response.getBody());

        verify(service, times(1)).getAllAuthorsDto();
    }

    @Test
    void getAuthorById_WhenAuthorFound_ShouldReturnOk() {
        AuthorDto author = new AuthorDto(1L, "Name", "Lastname", new HashSet<>(1));
        when(service.getAuthorDtoById(1L)).thenReturn(Optional.of(author));

        ResponseEntity<AuthorDto> response = controller.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author, response.getBody());

        verify(service, times(1)).getAuthorDtoById(anyLong());
    }

    @Test
    void getAuthorById_WhenAuthorNotFound_ShouldReturnNotFound() {
        when(service.getAuthorDtoById(1L)).thenReturn(Optional.empty());

        ResponseEntity<AuthorDto> response = controller.getAuthorById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(service, times(1)).getAuthorDtoById(anyLong());
    }

    @Test
    void getAuthorBooks_WhenBooksFound_ShouldReturnOk() {
        BookDto book= new BookDto(1L, "Title", List.of(1L),
                "Fictional", "Description", "available");
        when(service.getAuthorBooksDto(1L)).thenReturn(List.of(book));

        ResponseEntity<List<BookDto>> response = controller.getAuthorBooks(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(book), response.getBody());

        verify(service, times(1)).getAuthorBooksDto(anyLong());
    }

    @Test
    void getAuthorBooks_WhenNoBookFound_ShouldReturnNoContent() {
        when(service.getAuthorBooksDto(1L)).thenReturn(List.of());

        ResponseEntity<List<BookDto>> response = controller.getAuthorBooks(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAuthorBooksDto(anyLong());
    }

    @Test
    void getAuthorBooks_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        when(service.getAuthorBooksDto(1L)).thenThrow(new AuthorNotFoundException("Author with id 1 not found"));

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.getAuthorBooks(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(service, times(1)).getAuthorBooksDto(anyLong());
    }

    @Test
    void deleteAuthor_WhenAuthorFound_ShouldReturnNoContent(){
        ResponseEntity<Void> response = controller.deleteAuthor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).deleteAuthor(1L);
    }

    @Test
    void deleteAuthor_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException(){
        doThrow(new AuthorNotFoundException("Author with id 1 not found")).when(service).deleteAuthor(1L);

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.deleteAuthor(1L));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(service, times(1)).deleteAuthor(1L);
    }

    @Test
    void updateAuthor_WhenValidRequestAuthorFound_ShouldReturnOk(){
        AuthorDto updatedAuthor = new AuthorDto(1L, "First Name", "Last Name", Set.of());

        ResponseEntity<Void> response = controller.updateAuthor(1L, updatedAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }

    @Test
    void updateAuthor_WhenInvalidRequest_ShouldThrowAuthorValidationException(){
        AuthorDto author = new AuthorDto(null, "", "", null);

        when(validator.validate(author, UpdateAuthor.class)).thenThrow(new AuthorValidationException("Author update validation failed"));

        AuthorValidationException exception = assertThrows(AuthorValidationException.class,
                () -> controller.updateAuthor(1L, author));

        assertEquals("Author update validation failed", exception.getMessage());

        verify(validator, times(1)).validate(author, UpdateAuthor.class);
    }

    @Test
    void updateAuthor_WhenValidRequestAuthorNotFound_ShouldThrowAuthorNotFoundException(){
        AuthorDto updatedAuthor = new AuthorDto(1L, "First Name", "Last Name", Set.of());

        doThrow(new AuthorNotFoundException("Author with id 1 not found")).when(service).updateAuthor(1L, updatedAuthor);

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.updateAuthor(1L, updatedAuthor));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }
}