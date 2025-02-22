package org.library.library_app.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.library.library_app.dto.UserDto;
import org.library.library_app.entity.User;
import org.library.library_app.repository.UserRepository;
import org.library.library_app.tools.UserMapper;
import org.library.library_app.tools.UserType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto addUser(UserDto userDtoWithPassword) {
        User user = userMapper.userFromDto(userDtoWithPassword);
        return userMapper.userToDtoWithoutPassword(userRepository.saveAndFlush(user));
    }

    public List<UserDto> getAllUsersDto() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllUsersByType(UserType userType) {
        return userRepository.findByUserType(userType)
                .stream()
                .map(userMapper::userToDtoWithoutPassword)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userMapper.userToDtoWithoutPassword(userRepository.findById(userId).orElseThrow());
    }

}
