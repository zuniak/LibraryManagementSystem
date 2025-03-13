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

    @Null(groups = CreateAuthor.class, message = "ID must be null when creating a new author.")
    @NotNull(groups = UpdateAuthor.class, message = "ID is required when updating an author.")
    private Long id;

    @NotBlank(groups = {CreateAuthor.class, UpdateAuthor.class}, message = "First name cannot be blank.")
    private String firstName;

    @NotBlank(groups = {CreateAuthor.class, UpdateAuthor.class}, message = "Last name cannot be blank.")
    private String lastName;

    @Null(groups = CreateAuthor.class, message = "Books IDs must be null when creating a new author.")
    @NotNull(groups = UpdateAuthor.class, message = "Books IDs cannot be null when updating an author.")
    private Set<Long> booksIds;
}