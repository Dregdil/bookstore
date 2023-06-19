package maruszewski.com.bookstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.components.BasketModelAssembler;
import maruszewski.com.bookstore.components.BookModelAssembler;
import maruszewski.com.bookstore.dtos.BasketBooksDto;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.service.BasketService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@SecurityRequirement(name = "security_auth")
@ExposesResourceFor(Basket.class)
@RequestMapping(value = "/basket", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
@RequiredArgsConstructor

public class BasketController {
    private final BasketService basketService;
    private final BasketModelAssembler basketModelAssembler;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> bookPagedResourcesAssembler;

    @GetMapping("{id}")
    @Operation(summary = "Get Basket")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#id).user.login == authentication.name")
    public EntityModel<Basket> getSingleBasket(@PathVariable("id") Long id) {
        Basket basket = basketService.getSingleBasket(id);
        return basketModelAssembler.toModel(basket);
    }

    @GetMapping("{id}/books")
    @Operation(summary = "Get Basket books list")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#id).user.login == authentication.name")
    public PagedModel<EntityModel<Book>> getBasketBooks(@PathVariable("id") Long id, @ParameterObject Pageable page, @RequestParam(required = false, name = "page") Integer p,
                                                             @RequestParam(required = false) Integer size,
                                                             @RequestParam(required = false) String[] sort) {
        Page<Book> booksPage = basketService.getBasketBooks(id, page);
        PagedModel<EntityModel<Book>> bookPage = bookPagedResourcesAssembler.toModel(booksPage, bookModelAssembler);
        bookPage.getContent().forEach((book) -> book.mapLink(IanaLinkRelations.SELF, link -> link.andAffordance(afford(methodOn(BasketController.class).deleteBookFromBasket(null)))));
        return bookPage;
    }

    @DeleteMapping("/book/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#basketBooksDto.basketId).user.login == authentication.name")
    @Operation(summary = "From Basket delete Book")
    public ResponseEntity<Basket> deleteBookFromBasket(@RequestBody BasketBooksDto basketBooksDto) {
        return ResponseEntity.of(basketService.deleteBookFromBasket(basketBooksDto));
    }

    @PostMapping("/book/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#basketBooksDto.basketId).user.login == authentication.name")
    @Operation(summary = "To Basket add Book")
    public ResponseEntity<Basket> addBookToBasket(@RequestBody BasketBooksDto basketBooksDto) {
        return ResponseEntity.of(basketService.addBookToBasket(basketBooksDto));
    }

    @PutMapping("{id}/clear")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#id).user.login == authentication.name")
    @Operation(summary = "Clear Basket")
    public ResponseEntity<Basket> clearBasket(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(basketService.clearBasket(id));
    }

    @PostMapping("{id}/order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @basketService.getSingleBasket(#id).user.login == authentication.name")
    @Operation(summary = "Create Order")
        public ResponseEntity<Orders> postOrder(@PathVariable("id") Long id)
        {
            return ResponseEntity.ok(basketService.postOrder(id));
        }

}
