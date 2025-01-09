package org.library.library_app.repository;


import org.library.library_app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    List<Book> findBySeriesNameNotNull();

    List<Book> findBySeriesName(String seriesName);

    List<Book> findBySeriesNameAndSeriesNumber(String seriesName, Integer seriesNumber);

}
