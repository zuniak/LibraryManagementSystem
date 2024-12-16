package org.library.library_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.library.library_app.validationgroups.CreateAuthor;
import org.library.library_app.validationgroups.UpdateAuthor;

import java.util.Set;


@Data
@AllArgsConstructor
public class AuthorDto {
    @Null(groups = CreateAuthor.class)
    @NotNull(groups = UpdateAuthor.class)
    private Long id;

    @NotBlank(groups = {CreateAuthor.class, UpdateAuthor.class})
    private String firstName;

    @NotBlank(groups = {CreateAuthor.class, UpdateAuthor.class})
    private String lastName;

    @Null(groups = {CreateAuthor.class})
    @NotNull(groups = {UpdateAuthor.class})
    private Set<Long> booksIds;
}
