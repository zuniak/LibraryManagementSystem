package org.library.library_app.service;

import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Book;
import org.library.library_app.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.library.library_app.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(BookDto bookDto) {
        Book book = BookMapper.toEntity(bookDto);
        return bookRepository.saveAndFlush(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }
}
