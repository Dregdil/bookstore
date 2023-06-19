package maruszewski.com.bookstore.components;

import maruszewski.com.bookstore.controllers.BasketController;
import maruszewski.com.bookstore.models.Basket;
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
public class BasketModelAssembler implements RepresentationModelAssembler<Basket, EntityModel<Basket>> {
    @Override
    public EntityModel<Basket> toModel(Basket entity) {

        Link self = linkTo(methodOn(BasketController.class).getSingleBasket(entity.getId())).withSelfRel()
                .andAffordance(afford(methodOn(BasketController.class).clearBasket(entity.getId())))
                .andAffordance(afford(methodOn(BasketController.class).postOrder(entity.getId())));


        return EntityModel.of(entity).add(self);

    }
}