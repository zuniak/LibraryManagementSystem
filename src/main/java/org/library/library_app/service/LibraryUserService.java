package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.entity.LibraryUser;
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
    private final LibraryUserRepository libraryUserRepository;
    private final LibraryUserMapper libraryUserMapper;

    public LibraryUserDto addUser(LibraryUserDto libraryUserDto, Set<LibraryUserType> authorities) {
        LibraryUser user = libraryUserMapper.userFromDto(libraryUserDto);
        user.setAuthorities(authorities);
        return libraryUserMapper.userToDtoWithoutPassword(libraryUserRepository.saveAndFlush(user));
    }

    public List<LibraryUserDto> getAllUsersDto() {
        return libraryUserRepository.findAll()
                .stream()
                .map(libraryUserMapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public List<LibraryUserDto> getAllUsersByType(LibraryUserType libraryUserType) {
        return libraryUserRepository.findAll().stream()
                .filter(user -> user.getAuthorities().contains(libraryUserType))
                .map(libraryUserMapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public LibraryUserDto getUserDtoByUsername(String username) {
        Optional<LibraryUser> user = libraryUserRepository.findById(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("LibraryUser not found");
        }
        return libraryUserMapper.userToDtoWithoutPassword(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<LibraryUser> user = libraryUserRepository.findById(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("LibraryUser not found");
        }
        return user.get();
    }
}
