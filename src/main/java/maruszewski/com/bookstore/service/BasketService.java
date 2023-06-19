package maruszewski.com.bookstore.service;

import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.dtos.BasketBooksDto;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.repository.BasketRepository;
import maruszewski.com.bookstore.repository.BookRepository;
import maruszewski.com.bookstore.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public Basket getSingleBasket(Long id){
        return basketRepository.findById(id).orElseThrow();
    }

    public Page<Book> getBasketBooks(Long id, Pageable pageable) {
        Basket basket = basketRepository.findById(id).orElseThrow();
        List<Book> books = basket.getBooks();
        return new PageImpl<>(books);
    }

    public Optional<Basket> deleteBookFromBasket(BasketBooksDto basketBooksDto) {
        Optional<Basket> basket = basketRepository.findById(basketBooksDto.getBasketId());
        if(basket.isPresent()) {
            Optional<Book> book = bookRepository.findById(basketBooksDto.getBookId());
            if(book.isPresent()) {
                basket.get().getBooks().remove(book.get());
                return Optional.of(basketRepository.save(basket.get()));
            }
        }
        return Optional.empty();
    }

    public Optional<Basket> addBookToBasket(BasketBooksDto basketBooksDto) {
        Optional<Basket> basket = basketRepository.findById(basketBooksDto.getBasketId());
        if(basket.isPresent()) {
            Optional<Book> book = bookRepository.findById(basketBooksDto.getBookId());
            if(book.isPresent()) {
                basket.get().getBooks().add(book.get());
                return Optional.of(basketRepository.save(basket.get()));
            }
        }
        return Optional.empty();
    }

    public Basket clearBasket(Long id) {
        Basket basket = basketRepository.findById(id).orElseThrow();
        basket.setBooks(new ArrayList<>());
        return basketRepository.save(basket);
    }

    public Orders postOrder(Long id) {
        Basket basket = basketRepository.findById(id).orElseThrow();
        Orders order = new Orders();
        order.setBooks(new ArrayList<Book>(basket.getBooks()));
        order.setUser(basket.getUser());
        order.setStatus("IN PROGRESS");
        order.setCost(basketRepository.cost(id));
        clearBasket(id);
        return orderRepository.save(order);
    }
}
