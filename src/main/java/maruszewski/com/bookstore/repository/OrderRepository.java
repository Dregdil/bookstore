package maruszewski.com.bookstore.repository;
import maruszewski.com.bookstore.models.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByUserId(Long id);
    Page<Orders> findAllByUserId(Long id, Pageable page);
}
