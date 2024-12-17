package org.library.library_app.service;

import lombok.AllArgsConstructor;
import org.library.library_app.dto.AuthorDto;
import org.library.library_app.dto.BookDto;
import org.library.library_app.entity.Author;
import org.library.library_app.entity.Book;
import org.library.library_app.exceptions.AuthorNotFoundException;
import org.library.library_app.exceptions.BookNotFoundException;
import org.library.library_app.repository.AuthorRepository;
import org.library.library_app.repository.BookRepository;
import org.library.library_app.tools.AuthorMapper;
import org.library.library_app.tools.BookMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorDto addAuthor(AuthorDto authorDto) {
        Author author = toEntity(authorDto);
        return authorMapper.authorToDto(authorRepository.saveAndFlush(author));
    }

    public List<AuthorDto> getAllAuthorsDto() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::authorToDto)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDto> getAuthorDtoById(Long authorId) {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        return authorOpt.map(authorMapper::authorToDto);
    }

    public List<BookDto> getAuthorBooksDto(Long authorId) {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            return author.getBooks().stream()
                    .map(bookMapper::bookToDto)
                    .collect(Collectors.toList());
        }
        throw new AuthorNotFoundException("Author with id " + authorId + " not found");
    }


    public void deleteAuthor(Long id) {
        if (authorRepository.findById(id).isPresent()) {
            authorRepository.deleteById(id);
        } else {
            throw new AuthorNotFoundException("Author with id " + id + " not found");
        }
    }

    public void updateAuthor(Long id, AuthorDto updatedAuthor) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            author.setFirstName(updatedAuthor.getFirstName());
            author.setLastName(updatedAuthor.getLastName());
            updateAuthorBooks(author, updatedAuthor.getBooksIds());
            authorRepository.save(author);
        } else {
            throw new AuthorNotFoundException("Author with id " + id + " not found");
        }
    }

    private void updateAuthorBooks(Author author, Set<Long> booksIds) {
        List<Long> oldBooks = author.getBooks().stream()
                .map(Book::getId)
                .toList();

        List<Long> booksToRemove = oldBooks.stream()
                .filter(bookId -> !booksIds.contains(bookId))
                .toList();

        List<Long> booksToAdd = booksIds.stream()
                .filter(bookId -> !oldBooks.contains(bookId))
                .toList();


        for (Long bookId : booksToRemove) {
            Optional<Book> bookOpt = bookRepository.findById(bookId);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                author.removeBook(book);
                book.removeAuthor(author);
                if (book.getAuthors().isEmpty()) {
                    bookRepository.deleteById(bookId);
                }
            }
        }

        for (Long bookId : booksToAdd) {
            Optional<Book> bookOpt = bookRepository.findById(bookId);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                author.addBook(book);
                book.addAuthor(author);
            } else {
                throw new BookNotFoundException("Book with id " + bookId + " not found");
            }
        }
    }

    public Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.getFirstName(), authorDto.getLastName());
    }
}
