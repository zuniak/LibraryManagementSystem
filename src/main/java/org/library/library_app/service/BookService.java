package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookIdDoNotMatchException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.BookMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private BookMapper bookMapper;


    public BookDto addBook(BookDto bookDto) {
        Book book = bookMapper.bookFromDto(bookDto);
        addAuthors(book, bookDto.getAuthorsIds());
        return bookMapper.bookToDto(bookRepository.saveAndFlush(book));
    }

    public List<BookDto> getAllBooksDto() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksDtoThatArePartOfSeries() {
        return bookRepository.findBySeriesNameNotNull()
                .stream()
                .map(bookMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksDtoBySeriesName(String seriesName) {
        return bookRepository.findBySeriesName(seriesName)
                .stream()
                .map(bookMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksBySeriesNameAndNumber(String seriesName, Integer seriesNumber) {
        return bookRepository.findBySeriesNameAndSeriesNumber(seriesName, seriesNumber)
                .stream()
                .map(bookMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public Optional<BookDto> getBookDto(Long id) {
        return bookRepository.findById(id).map(bookMapper::bookToDto);
    }

    public List<BookDto> getBooksDtoByTitle(String title) {
        return bookRepository.findByTitle(title)
                .stream()
                .map(bookMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public void deleteBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            for (Author author : bookOpt.get().getAuthors()) {
                author.removeBook(bookOpt.get());
            }
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
    }

    public void updateBook(Long id, BookDto bookDto) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (!bookDto.getId().equals(id)) {
                throw new BookIdDoNotMatchException("Book id does not match");
            }
            bookMapper.updateBookFromDto(book, bookDto);

            for (Author author : book.getAuthors()) {
                author.removeBook(book);
            }
            book.getAuthors().clear();
            addAuthors(book, bookDto.getAuthorsIds());

            bookRepository.save(book);
        } else {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
    }

    private void addAuthors(Book book, List<Long> authorsIds) {
        for (Long authorId : authorsIds) {
            Optional<Author> authorOpt = authorRepository.findById(authorId);
            if (authorOpt.isEmpty()) {
                throw new AuthorNotFoundException("Author with id " + authorId + " not found");
            }
            Author author = authorOpt.get();
            book.addAuthor(author);
            author.addBook(book);
        }
    }
}
