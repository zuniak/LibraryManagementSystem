package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class BookLoan {
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
    private Date loanDate;

    private Date returnDate;
}
