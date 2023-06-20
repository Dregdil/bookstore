package maruszewski.com.bookstore.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import maruszewski.com.bookstore.components.BasketModelAssembler;
import maruszewski.com.bookstore.components.OrderModelAssembler;
import maruszewski.com.bookstore.components.UserModelAssembler;
import maruszewski.com.bookstore.dtos.UserDto;
import maruszewski.com.bookstore.models.Basket;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.models.User;
import maruszewski.com.bookstore.repository.UserRepository;
import maruszewski.com.bookstore.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.mapstruct.factory.Mappers;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@RestController
@SecurityRequirement(name = "security_auth")
@ExposesResourceFor(User.class)
@RequestMapping(value = "/users", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private final UserService userService;

    @NonNull
    private final PagedResourcesAssembler<User> pagedUserAssembler;
    @NonNull
    private final UserModelAssembler userModelAssembler;
    @NonNull
    private final OrderModelAssembler orderModelAssembler;
    @NonNull
    private final BasketModelAssembler basketModelAssembler;
    @NonNull
    private final PagedResourcesAssembler<Orders> pagedOrderAssembler;


    @GetMapping
    @Operation(summary = "User list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedModel<EntityModel<User>> getUser(@ParameterObject Pageable pageable, @RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String[] sort) {
        Page<User> users = userService.findAll(pageable);
        PagedModel<EntityModel<User>> model = pagedUserAssembler.toModel(users, userModelAssembler);

        model.mapLink(IanaLinkRelations.SELF, link ->
                link.andAffordance(afford(methodOn(UserController.class).postUser(null, null)))
        );

        return model;
    }

    @PostMapping
    @Operation(summary = "Create User")
    public ResponseEntity<EntityModel<User>> postUser(@RequestBody @Valid UserDto data, @RequestHeader(required = false) @Parameter(hidden = true) String Accept) {
        User user = userService.postUser(data);

        return ResponseEntity.created(
                linkTo(methodOn(UserController.class).getSingleUser(user.getId())).toUri()
        ).body(Accept != null ? userModelAssembler.toModel(user) : null);
    }


    @GetMapping("{id}")
    @Operation(summary = "Get User")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.getSingle(#id).login == authentication.name")
    public EntityModel<User> getSingleUser(@PathVariable("id") Long id) {
        User user = userService.getSingle(id);
        return userModelAssembler.toModel(user);
    }


    @DeleteMapping("{id}")
    @Operation(summary = "Delete User")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.getSingle(#id).login == authentication.name")
    public ResponseEntity<EntityModel<User>> deleteUser(@PathVariable("id") Long id, @RequestHeader(required = false) String Accept) {
        var user = userService.getSingle(id);
        userService.delete(user);

        if (Accept == null) {
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @PutMapping("{id}")
    @Operation(summary = "Update User")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.getSingle(#id).login == authentication.name")
    public ResponseEntity<EntityModel<User>> patchUser(@PathVariable Long id, @RequestBody @Valid UserDto data, @RequestHeader(required = false) String Accept) {

        User user = userService.patchUser(id, data);

        if (Accept == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @GetMapping("{id}/basket")
    @Operation(summary = "Get User Basket")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.getSingle(#id).login == authentication.name")
    public EntityModel<Basket> getBasket(@PathVariable("id") Long id) {
        Basket basket = userService.getBasket(id);
        return basketModelAssembler.toModel(basket);
    }

    @GetMapping("{id}/orders")
    @Operation(summary = "Get User Order list")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.getSingle(#id).login == authentication.name")
    public PagedModel<EntityModel<Orders>> getOrders(@PathVariable("id") Long id, @ParameterObject Pageable page) {

        Page<Orders> orderPage = userService.getOrders(id,page);
        return pagedOrderAssembler.toModel(orderPage, orderModelAssembler);
    }
}
