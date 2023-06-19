package maruszewski.com.bookstore.service;


import maruszewski.com.bookstore.dtos.UserDto;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.models.User;
import maruszewski.com.bookstore.repository.BasketRepository;
import maruszewski.com.bookstore.repository.OrderRepository;
import maruszewski.com.bookstore.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final BasketRepository basketRepository;

    @NonNull
    private final OrderRepository orderRepository;

    private final PasswordEncoder passwordEncoder;

    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return userRepository.saveAll(entities);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }


    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User getSingle(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    public Optional<User> findByLogin(String login) {
        return  userRepository.findByLogin(login);
    }

    public User postUser(UserDto data) {
        User user = new User();
        user.setEmail(data.getEmail());
        user.setLogin(data.getLogin());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepository.save(user);
        Basket basket = new Basket();
        basket.setUser(user);
        basketRepository.save(basket);
        return user;
    }

    public User patchUser(Long id, UserDto data) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEmail(data.getEmail());
        user.setLogin(data.getLogin());
        user.setPassword(passwordEncoder.encode(data.getPassword()));

        return userRepository.save(user);
    }

    public Basket getBasket(Long id) {
        return basketRepository.findByUserId(id).orElseThrow();
    }

    public Page<Orders> getOrders(Long id, Pageable page) {
        return orderRepository.findAllByUserId(id, page);
    }
}

