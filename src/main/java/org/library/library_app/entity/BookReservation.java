package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class BookReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Book book;

    @NotBlank
    @ManyToOne
    private LibraryUser member;

    @NotNull
    private Date reservationDueDate;
}
