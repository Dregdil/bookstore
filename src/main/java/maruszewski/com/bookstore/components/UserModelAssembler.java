package maruszewski.com.bookstore.components;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import maruszewski.com.bookstore.controllers.UserController;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @NonNull
    private final EntityLinks links;
    @NonNull
    private final LinkRelationProvider linkRelationProvider;

    @Override
    public EntityModel<User> toModel(User entity) {


        Link self = links.linkToItemResource(entity, User::getId);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();



        if(auth.getName().equals(entity.getLogin()) || authorities.contains("ROLE_ADMIN")){
            self = self.andAffordance(afford(methodOn(UserController.class).deleteUser(entity.getId(), null)))
                    .andAffordance(afford(methodOn(UserController.class).patchUser(entity.getId(), null, null)));
        }

        HalModelBuilder builder = HalModelBuilder.halModelOf(entity)
                .link(self)
                .link(linkTo(methodOn(UserController.class).getBasket(entity.getId())).withRel(
                        linkRelationProvider.getCollectionResourceRelFor(Basket.class)
                ));

        return (EntityModel<User>) builder
                .build();
    }
}
