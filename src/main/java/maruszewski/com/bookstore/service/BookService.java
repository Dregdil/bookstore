package maruszewski.com.bookstore.service;

import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.dtos.BookDto;
import maruszewski.com.bookstore.errors.ResourceNotFoundException;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.repository.BasketRepository;
import maruszewski.com.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BasketRepository basketRepository;
    public Page<Book> getBook(Pageable page) {
        return bookRepository.findAll(page);
    }

    public Book getSingleBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        book.setTitle(bookDto.getTitle());
        book.setPrice(bookDto.getPrice());
        book.setAuthor(bookDto.getAuthor());

        return bookRepository.save(book);
    }

    public void deleteSingleBook(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        List<Basket> baskets = basketRepository.getBasketsByBooks(book);
        for(Basket b: baskets)
        {
            b.getBooks().removeAll(Collections.singleton(book));
        }

        bookRepository.deleteById(id);
    }
}

