package org.library.library_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String author;
}
