package org.library.library_app.tools;

import org.library.library_app.dto.UserDto;
import org.library.library_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userFromDto(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto userToDtoWithoutPassword(User user);
}
