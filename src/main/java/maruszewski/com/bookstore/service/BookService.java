package maruszewski.com.bookstore.service;

import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.dtos.BookDto;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Page<Book> getBook(Pageable page) {
        return bookRepository.findAll(page);
    }

    public Book getSingleBook(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public Book addSingleBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setPrice(bookDto.getPrice());
        book.setAuthor(bookDto.getAuthor());
        return bookRepository.save(book);
    }

    public Book updateSingleBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow();
        book.setTitle(bookDto.getTitle());
        book.setPrice(bookDto.getPrice());
        book.setAuthor(bookDto.getAuthor());

        return bookRepository.save(book);
    }

    public void deleteSingleBook(long id) {
        bookRepository.deleteById(id);
    }
}
