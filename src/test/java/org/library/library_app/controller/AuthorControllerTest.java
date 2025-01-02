package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.exceptions.AuthorIdDoNotMatchException;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.AuthorValidationException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.service.AuthorService;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.library.library_app.validationgroups.CreateAuthor;
import org.library.library_app.validationgroups.UpdateAuthor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {
    @Mock
    AuthorService service;

    @Mock
    Validator validator;

    @InjectMocks
    AuthorController controller;

    @BeforeEach
    void resetMocks() {
        reset(service);
        reset(validator);
    }

    @Test
    void addAuthor_WhenValidRequest_ShouldReturnCreated() {
        AuthorDto authorDto = AuthorMother.createDtoValidCreateAuthor();
        AuthorDto savedDto = AuthorMother.createDto(1L);

        when(validator.validate(authorDto, CreateAuthor.class)).thenReturn(Set.of());
        when(service.addAuthor(authorDto)).thenReturn(savedDto);

        ResponseEntity<AuthorDto> response = controller.addAuthor(authorDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedDto, response.getBody());

        verify(validator, times(1)).validate(authorDto, CreateAuthor.class);
        verify(service, times(1)).addAuthor(authorDto);
    }

    @Test
    void addAuthor_WhenInvalidRequest_ShouldThrowAuthorValidationException() {
        AuthorDto authorDto = AuthorMother.createDtoInvalidCreateAuthor();

        @SuppressWarnings("unchecked") // Mock used to make validator returns not empty set.
        ConstraintViolation<AuthorDto> violation = mock(ConstraintViolation.class);

        when(validator.validate(authorDto, CreateAuthor.class)).thenReturn(Set.of(violation));

        AuthorValidationException exception = assertThrows(AuthorValidationException.class,
                () -> controller.addAuthor(authorDto));

        assertEquals("Author create validation failed", exception.getMessage());

        verify(validator, times(1)).validate(authorDto, CreateAuthor.class);
        verify(service, never()).addAuthor(authorDto);
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
        AuthorDto authorDto = AuthorMother.createDto(1L);

        when(service.getAllAuthorsDto()).thenReturn(List.of(authorDto));

        ResponseEntity<List<AuthorDto>> response = controller.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(authorDto), response.getBody());

        verify(service, times(1)).getAllAuthorsDto();
    }

    @Test
    void getAuthorById_WhenAuthorFound_ShouldReturnOk() {
        AuthorDto authorDto = AuthorMother.createDto(1L);

        when(service.getAuthorDtoById(1L)).thenReturn(Optional.of(authorDto));

        ResponseEntity<AuthorDto> response = controller.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authorDto, response.getBody());

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
    void getAuthorBooks_WhenAuthorAndBooksFound_ShouldReturnOk() {
        BookDto bookDto = BookMother.createDto(1L, List.of(1L));

        when(service.getAuthorBooksDto(1L)).thenReturn(List.of(bookDto));

        ResponseEntity<List<BookDto>> response = controller.getAuthorBooks(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(bookDto), response.getBody());

        verify(service, times(1)).getAuthorBooksDto(anyLong());
    }

    @Test
    void getAuthorBooks_WhenBooksNotFound_ShouldReturnNoContent() {
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
    void updateAuthor_WhenValidRequestAndAuthorFound_ShouldReturnOk(){
        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(validator.validate(updatedAuthor, UpdateAuthor.class)).thenReturn(Set.of());

        ResponseEntity<Void> response = controller.updateAuthor(1L, updatedAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }

    @Test
    void updateAuthor_WhenValidRequestButAuthorNotFound_ShouldThrowAuthorNotFoundException(){
        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(validator.validate(updatedAuthor, UpdateAuthor.class)).thenReturn(Set.of());
        doThrow(new AuthorNotFoundException("Author with id 1 not found")).when(service).updateAuthor(1L, updatedAuthor);

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
                () -> controller.updateAuthor(1L, updatedAuthor));

        assertEquals("Author with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }

    @Test
    void updateAuthor_WhenValidRequestButIdDoNotMatch_ShouldThrowAuthorIdDoNotMatchException(){
        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(validator.validate(updatedAuthor, UpdateAuthor.class)).thenReturn(Set.of());
        doThrow(new AuthorIdDoNotMatchException("Author id does not match")).when(service).updateAuthor(2L, updatedAuthor);

        AuthorIdDoNotMatchException exception = assertThrows(AuthorIdDoNotMatchException.class,
                () -> controller.updateAuthor(2L, updatedAuthor));

        assertEquals("Author id does not match", exception.getMessage());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }

    @Test
    void updateAuthor_WhenValidRequestButBookNotFound_ShouldThrowBookNotFoundException(){
        AuthorDto updatedAuthor = AuthorMother.createDtoValidUpdateAuthor(1L, Set.of(1L));

        when(validator.validate(updatedAuthor, UpdateAuthor.class)).thenReturn(Set.of());
        doThrow(new BookNotFoundException("Book with id 1 not found")).when(service).updateAuthor(1L, updatedAuthor);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> controller.updateAuthor(1L, updatedAuthor));

        assertEquals("Book with id 1 not found", exception.getMessage());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
        verify(service, times(1)).updateAuthor(anyLong(), any(AuthorDto.class));
    }

    @Test
    void updateAuthor_WhenInvalidRequest_ShouldThrowAuthorValidationException(){
        AuthorDto updatedAuthor = AuthorMother.createDtoInvalidUpdateAuthor();

        @SuppressWarnings("unchecked") // Mock used to make validator returns not empty set.
        ConstraintViolation<AuthorDto> violation = mock(ConstraintViolation.class);

        when(validator.validate(updatedAuthor, UpdateAuthor.class)).thenReturn(Set.of(violation));

        AuthorValidationException exception = assertThrows(AuthorValidationException.class,
                () -> controller.updateAuthor(1L, updatedAuthor));

        assertEquals("Author update validation failed", exception.getMessage());

        verify(validator, times(1)).validate(updatedAuthor, UpdateAuthor.class);
    }
}
