package org.library.library_app.repository;

import org.library.library_app.entity.Book;
import org.library.library_app.entity.BookReservation;
import org.library.library_app.entity.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    Optional<BookReservation> findTopByBookOrderByStartDate(Book book);
    Optional<BookReservation> findByBookAndMember(Book book, LibraryUser member);
    Integer countBookReservationsByMember(LibraryUser member);
    Integer countBookReservationsByBook(Book book);
}
