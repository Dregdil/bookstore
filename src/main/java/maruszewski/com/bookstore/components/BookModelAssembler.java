package maruszewski.com.bookstore.components;

import maruszewski.com.bookstore.controllers.BookController;
import maruszewski.com.bookstore.dtos.BookDto;
import maruszewski.com.bookstore.models.Book;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;

import java.util.Collection;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
    @Override
    public EntityModel<Book> toModel(Book entity) {

        Link self = linkTo(methodOn(BookController.class).getSingleBook(entity.getId())).withSelfRel();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if(authorities.contains("ROLE_ADMIN")){

            self = self        .andAffordance(afford(methodOn(BookController.class).deleteSingleBook(entity.getId())))
                    .andAffordance(afford(methodOn(BookController.class).updateSingleBook(entity.getId(), new BookDto())));

        }


        return EntityModel.of(entity).add();

    }
}