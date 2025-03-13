package org.library.library_app.tools;

import org.library.library_app.dto.LibraryUserDto;
import org.library.library_app.entity.LibraryUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LibraryUserMapper {

    @Mapping(target = "authorities", ignore = true)
    LibraryUser userFromDto(LibraryUserDto libraryUserDto);

    @Mapping(target = "password", ignore = true)
    LibraryUserDto userToDtoWithoutPassword(LibraryUser libraryUser);
}
