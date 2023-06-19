package maruszewski.com.bookstore.repository;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUserId(Long id);
    @Query("SELECT SUM (a.price) FROM Basket b join b.books a WHERE b.id = :id")
    Double cost(Long id);

    List<Basket> getBasketsByBooks(Book book);
}
