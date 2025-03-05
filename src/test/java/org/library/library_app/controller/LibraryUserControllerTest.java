package org.library.library_app.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.exceptions.UserValidationException;
import org.library.library_app.service.LibraryUserService;
import org.library.library_app.tools.LibraryUserType;
import org.library.library_app.validationgroups.CreateUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryUserControllerTest {
    @Mock
    LibraryUserService service;

    @Mock
    Validator validator;

    @InjectMocks
    LibraryUserController controller;

    LibraryUserDto memberDto;
    LibraryUserDto employeeDto;

    @BeforeEach
    void setUp() {
        reset(service);
        reset(validator);

        memberDto = new LibraryUserDto();
        employeeDto = new LibraryUserDto();
    }

    @Test
    void addEmployee_whenValidEmployee_thenReturnsCreated() {
        memberDto.setUsername("username");
        when(validator.validate(employeeDto, CreateUser.class)).thenReturn(Set.of());

        ResponseEntity<String> response = controller.addEmployee(memberDto);

        assertEquals("Employee " + memberDto.getUsername() + " created", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(validator, times(1)).validate(employeeDto, CreateUser.class);
        verify(service, times(1)).addUser(employeeDto, Set.of(LibraryUserType.EMPLOYEE));
    }

    @Test
    void addEmployee_whenInvalidEmployee_thenThrowsException() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<LibraryUserDto> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<LibraryUserDto>> violations = Set.of(violation);

        when(validator.validate(employeeDto, CreateUser.class)).thenReturn(violations);

        UserValidationException exception = assertThrows(UserValidationException.class, () -> controller.addEmployee(employeeDto));

        assertEquals("Employee create validation failed", exception.getMessage());
        assertEquals(violations, exception.getViolations());

        verify(validator, times(1)).validate(employeeDto, CreateUser.class);
        verify(service, never()).addUser(employeeDto, Set.of(LibraryUserType.EMPLOYEE));
    }

    @Test
    void getAllUsers_whenUsersExist_thenReturnsOk() {
        when(service.getAllUsersDto()).thenReturn(List.of(memberDto, employeeDto));

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllUsers();

        assertEquals(List.of(memberDto, employeeDto), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).getAllUsersDto();
    }

    @Test
    void getAllUsers_whenNoUsersExist_thenReturnsNoContent() {
        when(service.getAllUsersDto()).thenReturn(List.of());

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAllUsersDto();
    }

    @Test
    void getAllMembers_whenMembersExist_thenReturnsOk() {
        when(service.getAllUsersByType(LibraryUserType.MEMBER)).thenReturn(List.of(memberDto));

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllMembers();

        assertEquals(List.of(memberDto), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).getAllUsersByType(LibraryUserType.MEMBER);
    }

    @Test
    void getAllMembers_whenNoMembersExist_thenReturnsNoContent() {
        when(service.getAllUsersByType(LibraryUserType.MEMBER)).thenReturn(List.of());

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllMembers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAllUsersByType(LibraryUserType.MEMBER);
    }

    @Test
    void getAllEmployees_whenEmployeesExist_thenReturnsOk() {
        when(service.getAllUsersByType(LibraryUserType.EMPLOYEE)).thenReturn(List.of(employeeDto));

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllEmployees();

        assertEquals(List.of(employeeDto), response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).getAllUsersByType(LibraryUserType.EMPLOYEE);
    }

    @Test
    void getAllEmployees_whenNoEmployeesExist_thenReturnsNoContent() {
        when(service.getAllUsersByType(LibraryUserType.EMPLOYEE)).thenReturn(List.of());

        ResponseEntity<List<LibraryUserDto>> response = controller.getAllEmployees();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(service, times(1)).getAllUsersByType(LibraryUserType.EMPLOYEE);
    }

    @Test
    void getUserById_whenUserExists_thenReturnsOk() {
        when(service.getUserDtoByUsername("username")).thenReturn(memberDto);

        ResponseEntity<LibraryUserDto> response = controller.getUserById("username");

        assertEquals(memberDto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).getUserDtoByUsername("username");
    }

    @Test
    void getUserById_whenServiceThrowsException_thenThrowsException() {
        when(service.getUserDtoByUsername("username")).thenThrow(new UsernameNotFoundException(""));

        assertThrows(UsernameNotFoundException.class, () -> controller.getUserById("username"));

        verify(service, times(1)).getUserDtoByUsername("username");
    }
}