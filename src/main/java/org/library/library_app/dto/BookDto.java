package org.library.library_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.library.library_app.tools.BookCategory;
import org.library.library_app.tools.BookStatus;
import org.library.library_app.validationgroups.CreateBook;
import org.library.library_app.validationgroups.UpdateBook;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {

    @Null(groups = CreateBook.class, message = "ID must be null when creating a new book.")
    @NotNull(groups = UpdateBook.class, message = "ID is required when updating a book.")
    private Long id;

    @NotBlank(groups = {CreateBook.class, UpdateBook.class}, message = "Title cannot be blank.")
    private String title;

    @NotEmpty(groups = {CreateBook.class, UpdateBook.class}, message = "Authors list cannot be empty.")
    private List<Long> authorsIds;

    @NotNull(groups = {CreateBook.class, UpdateBook.class}, message = "Category cannot be null.")
    private BookCategory category;

    @NotBlank(groups = {CreateBook.class, UpdateBook.class}, message = "Description cannot be blank.")
    private String description;

    private String seriesName;

    private Integer seriesNumber;

    @Null(groups = {CreateBook.class}, message = "Status must be null when creating a new book.")
    @NotNull(groups = {UpdateBook.class}, message = "Status is required when updating a book.")
    private BookStatus status;
}
