package org.library.library_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.library.library_app.validationgroups.CreateBookReservationOrLoan;

import java.util.Date;

@Data
public class BookReservationDto {
    @Null(groups = CreateBookReservationOrLoan.class, message = "Reservation ID must be null when creating a new book reservation.")
    private Long id;

    @NotNull(groups = CreateBookReservationOrLoan.class, message = "Book ID must not be null when creating a new book loan.")
    private Long bookId;

    @NotBlank(groups = CreateBookReservationOrLoan.class, message = "Member username cannot be blank.")
    private String memberUsername;

    @Null(groups = CreateBookReservationOrLoan.class, message = "Reservation due date must be null when creating a new book reservation.")
    private Date reservationDueDate;
}
