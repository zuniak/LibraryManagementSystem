package org.library.library_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.repository.LibraryUserRepository;
import org.library.library_app.tools.LibraryUserMapper;
import org.library.library_app.tools.LibraryUserType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryUserServiceTest {
    @Mock
    LibraryUserRepository repository;

    @Mock
    LibraryUserMapper mapper;

    @InjectMocks
    LibraryUserService service;


    LibraryUser member;
    LibraryUserDto memberDto;

    LibraryUser employee;
    LibraryUserDto employeeDto;

    @BeforeEach
    void restartMocks() {
        reset(repository, mapper);
        member = new LibraryUser();
        memberDto = new LibraryUserDto();
        employee = new LibraryUser();
        employeeDto = new LibraryUserDto();
    }

    @Test
    void addUser() {
        Set<LibraryUserType> authorities = Set.of(LibraryUserType.MEMBER);
        member.setAuthorities(authorities);

        LibraryUserDto savedUserDto = new LibraryUserDto();

        when(mapper.userFromDto(memberDto)).thenReturn(member);
        when(repository.saveAndFlush(member)).thenReturn(member);
        service.addUser(memberDto, authorities);

        assertEquals(authorities, member.getAuthorities());

        verify(mapper, times(1)).userFromDto(memberDto);
        verify(repository, times(1)).saveAndFlush(member);
    }

    @Test
    void getAllUsersDto() {
        when(repository.findAll()).thenReturn(List.of(member, employee));
        when(mapper.userToDtoWithoutPassword(member)).thenReturn(memberDto);
        when(mapper.userToDtoWithoutPassword(employee)).thenReturn(employeeDto);

        List<LibraryUserDto> result = service.getAllUsersDto();

        assertEquals(List.of(memberDto, employeeDto), result);

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).userToDtoWithoutPassword(any(LibraryUser.class));
    }

    @Test
    void getAllUsersByType() {
        member.setAuthorities(Set.of(LibraryUserType.MEMBER));
        employee.setAuthorities(Set.of(LibraryUserType.EMPLOYEE));

        when(repository.findByAuthoritiesContaining(LibraryUserType.MEMBER)).thenReturn(List.of(member));
        when(mapper.userToDtoWithoutPassword(member)).thenReturn(memberDto);

        List<LibraryUserDto> result = service.getAllUsersByType(LibraryUserType.MEMBER);

        assertEquals(List.of(memberDto), result);

        verify(repository, times(1)).findByAuthoritiesContaining(LibraryUserType.MEMBER);
        verify(mapper, times(1)).userToDtoWithoutPassword(member);
    }

    @Test
    void getUserDtoByUsername_whenUserFound_shouldReturnUserDto() {
        when(repository.findById("username")).thenReturn(Optional.of(member));
        when(mapper.userToDtoWithoutPassword(member)).thenReturn(memberDto);

        LibraryUserDto result = service.getUserDtoByUsername("username");

        assertEquals(memberDto, result);

        verify(repository, times(1)).findById("username");
        verify(mapper, times(1)).userToDtoWithoutPassword(member);
    }

    @Test
    void getUserDtoByUsername_whenUserNotFound_shouldThrowUsernameNotFoundException() {
        when(repository.findById("username")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.getUserDtoByUsername("username"));

        verify(repository, times(1)).findById("username");
        verify(mapper, times(0)).userToDtoWithoutPassword(any());
    }

    @Test
    void loadUserByUsername_whenUserFound_shouldReturnUserDetails() {
        when(repository.findById("username")).thenReturn(Optional.of(member));

        UserDetails result = service.loadUserByUsername("username");

        assertEquals(member, result);

        verify(repository, times(1)).findById("username");
    }

    @Test
    void loadUserByUsername_whenUserNotFound_shouldThrowUsernameNotFoundException() {
        when(repository.findById("username")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("username"));

        verify(repository, times(1)).findById("username");
    }
}
