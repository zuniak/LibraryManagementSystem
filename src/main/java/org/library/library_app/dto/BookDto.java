package org.library.library_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {
    @Null(groups = CreateBook.class)
    @NotNull(groups = UpdateBook.class)
    private Long id;

    @NotBlank(groups = {CreateBook.class, UpdateBook.class})
    private String title;

    @NotEmpty(groups = {CreateBook.class, UpdateBook.class})
    private List<Long> authorsIds;

    @NotNull(groups = {CreateBook.class, UpdateBook.class})
    private String category;

    @NotBlank(groups = {CreateBook.class, UpdateBook.class})
    private String description;

    @NotNull(groups = {UpdateBook.class})
    private String status;
}
