package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.exceptions.UsernameIsTakenException;
import org.library.library_app.repository.LibraryUserRepository;
import org.library.library_app.tools.LibraryUserMapper;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LibraryUserService implements UserDetailsService {
    private final LibraryUserRepository repository;
    private final LibraryUserMapper mapper;

    public void addUser(LibraryUserDto userDto, Set<LibraryUserType> authorities) {
        if (repository.existsById(userDto.getUsername())) {
            throw new UsernameIsTakenException("Library user " + userDto.getUsername() + " already exists");
        }
        LibraryUser user = mapper.userFromDto(userDto);
        user.setAuthorities(authorities);
        repository.saveAndFlush(user);
    }

    public List<LibraryUserDto> getAllUsersDto() {
        return repository.findAll()
                .stream()
                .map(mapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public List<LibraryUserDto> getAllUsersByType(LibraryUserType libraryUserType) {
        return repository.findByAuthoritiesContaining(libraryUserType)
                .stream()
                .map(mapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public LibraryUserDto getUserDtoByUsername(String username) {
        Optional<LibraryUser> user = repository.findById(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("LibraryUser not found");
        }
        return mapper.userToDtoWithoutPassword(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<LibraryUser> user = repository.findById(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("LibraryUser not found");
        }
        return user.get();
    }
}
