package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.library.library_app.tools.BookCategory;
import org.library.library_app.tools.BookStatus;

import java.util.LinkedList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    @NotEmpty
    private List<Author> authors;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookCategory category;

    @NotBlank
    private String description;

    private String seriesName;

    private Integer seriesNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    public Book(String title, BookCategory category, String description) {
        this.title = title;
        this.authors = new LinkedList<>();
        this.category = category;
        this.description = description;
        this.status = BookStatus.AVAILABLE;
    }
}
