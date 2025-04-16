package org.library.library_app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.entity.BookReservation;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.testdata.AuthorMother;
import org.library.library_app.testdata.BookMother;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookReservationRepositoryTest {
    @Autowired
    BookReservationRepository reservationRepository;

    @Autowired
    TestEntityManager entityManager;

    Book book;
    LibraryUser member1;
    LibraryUser member2;

    @BeforeEach
    void setUp() {
        Author author = AuthorMother.createAuthor();
        entityManager.persist(author);

        book = BookMother.createBook(List.of(author));
        entityManager.persist(book);

        member1 = new LibraryUser();
        member1.setAuthorities(Set.of(LibraryUserType.MEMBER));
        member1.setFirstName("Member");
        member1.setLastName("Member");
        member1.setEmail("member@member.com");
        member1.setUsername("member");
        member1.setPhoneNumber("123456789");
        member1.setPassword("password");
        entityManager.persist(member1);

        member2 = new LibraryUser();
        member2.setAuthorities(Set.of(LibraryUserType.MEMBER));
        member2.setFirstName("Member2");
        member2.setLastName("Member2");
        member2.setEmail("member2@member.com");
        member2.setUsername("member2");
        member2.setPhoneNumber("123456789");
        member2.setPassword("password2");
        entityManager.persist(member2);


        BookReservation bookReservation = new BookReservation(book, member1);
        entityManager.persist(bookReservation);

        BookReservation bookReservation2 = new BookReservation(book, member2);
        entityManager.persist(bookReservation2);
    }

    @Test
    void findByBookOrderByStartDate(){
        Optional<BookReservation> bookReservationOpt = reservationRepository.findTopByBookOrderByStartDate(book);
        assertTrue(bookReservationOpt.isPresent());
        BookReservation bookReservation = bookReservationOpt.get();
        assertEquals(member1, bookReservation.getMember());
    }
}
