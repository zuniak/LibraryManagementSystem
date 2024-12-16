package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.BookCategory;
import org.library.library_app.tools.DtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private AuthorRepository authorRepository;
    private BookRepository repository;

    public BookDto addBook(BookDto bookData) {
        Book book = this.toEntity(bookData);
        return DtoMapper.bookToDto(repository.saveAndFlush(book));
    }

    public List<BookDto> getAllBooksDto() {
        return repository.findAll()
                .stream()
                .map(DtoMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public Optional<BookDto> getBookDto(Long id) {
        return repository.findById(id).map(DtoMapper::bookToDto);
    }

    public boolean deleteBook(Long id) {
        Optional<Book> bookOpt = repository.findById(id);
        if (bookOpt.isPresent()) {
            for (Author author : bookOpt.get().getAuthors()) {
                author.removeBook(bookOpt.get());
            }
            repository.deleteById(id);
            return true;
        }
        throw new IllegalArgumentException("Book with id " + id + " not found");
    }

    public boolean updateBook(Long id, BookDto bookData) {
        Optional<Book> bookOpt = repository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setTitle(bookData.getTitle());

            for (Author author : book.getAuthors()) {
                author.removeBook(book);
                book.removeAuthor(author);
            }
            addAuthors(book, bookData.getAuthorsIds());

            book.setCategory(BookCategory.fromString(bookData.getCategory()));
            book.setDescription(bookData.getDescription());
            repository.save(book);
            return true;
        }
        throw new IllegalArgumentException("Book with id " + id + " not found");
    }

    private void addAuthors(Book book, List<Long> authorsIds) {
        for (Long authorId : authorsIds) {
            Optional<Author> authorOpt = authorRepository.findById(authorId);
            if (authorOpt.isEmpty()) {
                throw new IllegalArgumentException("Author with id " + authorId + " not found");
            }
            Author author = authorOpt.get();
            book.addAuthor(author);
            author.addBook(book);
            authorRepository.save(author);
        }
    }

    public Book toEntity(BookDto bookData) {
        Book book = new Book(bookData.getTitle(), BookCategory.fromString(bookData.getCategory()), bookData.getDescription());
        addAuthors(book, bookData.getAuthorsIds());
        return book;
    }
}
