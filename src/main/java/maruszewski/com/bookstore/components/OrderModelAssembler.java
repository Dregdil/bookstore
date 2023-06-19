package maruszewski.com.bookstore.components;

import maruszewski.com.bookstore.controllers.OrderController;
import maruszewski.com.bookstore.dtos.OrderStatusDto;
import maruszewski.com.bookstore.models.Orders;
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
public class OrderModelAssembler implements RepresentationModelAssembler<Orders, EntityModel<Orders>> {
    @Override
    public EntityModel<Orders> toModel(Orders entity) {

        Link self = linkTo(methodOn(OrderController.class).getSingleOrder(entity.getId())).withSelfRel();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if(authorities.contains("ROLE_ADMIN")){

            self = self        .andAffordance(afford(methodOn(OrderController.class).deleteSingleOrder(entity.getId())))
                    .andAffordance(afford(methodOn(OrderController.class).updateOrder(entity.getId(), new OrderStatusDto())));

        }


        return EntityModel.of(entity).add(self);

    }
}