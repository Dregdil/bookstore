package maruszewski.com.bookstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.components.BookModelAssembler;
import maruszewski.com.bookstore.dtos.BookDto;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.service.BookService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @SecurityRequirement(name="security_auth")
@RequiredArgsConstructor
@RequestMapping(value="/book", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
@ExposesResourceFor(Book.class)
public class BookController {
    private final BookService bookService;
    private final BookModelAssembler bookModelAssembler;
    private final PagedResourcesAssembler<Book> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary="Get Book list")
    public PagedModel<EntityModel<Book>> getArtists(@ParameterObject Pageable page, @RequestParam(required = false, name = "page") Integer p,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String[] sort) {
        Page<Book> bookPage = bookService.getBook(page);
        return pagedResourcesAssembler.toModel(bookPage, bookModelAssembler);
    }

    @GetMapping("{id}")    @Operation(summary="Get Book")
    public EntityModel<Book> getSingleBook(@PathVariable("id") Long id) {
        Book book = bookService.getSingleBook(id);
        return bookModelAssembler.toModel(book);
    }

    @PostMapping
    @Operation(summary="Create Book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Book addSingleBook(@RequestBody BookDto bookDto) {
        return bookService.addSingleBook(bookDto);
    }

    @PutMapping("{id}")
    @Operation(summary="Update Book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Book> updateSingleBook(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        Book book = bookService.updateSingleBook(id, bookDto);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("{id}")
    @Operation(summary="Delete Book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Book> deleteSingleBook(@PathVariable("id") Long id) {
        bookService.deleteSingleBook(id);
        return ResponseEntity.ok(null);
    }
}
