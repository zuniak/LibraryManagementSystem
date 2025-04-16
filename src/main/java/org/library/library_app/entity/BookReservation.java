package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class BookReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Book book;

    @NotNull
    @ManyToOne
    private LibraryUser member;

    @NotNull
    private Date startDate;

    private Date reservationDueDate;

    private Long fine;

    public BookReservation(Book book, LibraryUser member) {
        this.book = book;
        this.member = member;
        this.startDate = new Date();
    }
}
