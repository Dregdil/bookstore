package maruszewski.com.bookstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.components.OrderModelAssembler;
import maruszewski.com.bookstore.dtos.OrderStatusDto;
import maruszewski.com.bookstore.models.Book;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping(value="/order", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
@ExposesResourceFor(Book.class)
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<Orders> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary="Get Order list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PagedModel<EntityModel<Orders>> getOrders(@ParameterObject Pageable page, @RequestParam(required = false, name = "page") Integer p,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String[] sort) {
        Page<Orders> orderPage = orderService.getOrder(page);
        return pagedResourcesAssembler.toModel(orderPage, orderModelAssembler);
    }

    @GetMapping("{id}")
    @Operation(summary="Get order")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @orderService.getSingleOrder(#id).user.login == authentication.name")
    public EntityModel<Orders> getSingleOrder(@PathVariable("id") Long id) {
        Orders order = orderService.getSingleOrder(id);
        return orderModelAssembler.toModel(order);
    }

    @PutMapping("{id}")
    @Operation(summary="Update Order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Orders> updateOrder(@PathVariable("id") Long id, @RequestBody OrderStatusDto OrderStatusDto) {
        Orders order = orderService.updateSingleOrder(id, OrderStatusDto);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("{id}")
    @Operation(summary="Delete Order")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Orders> deleteSingleOrder(@PathVariable("id") Long id) {
        orderService.deleteSingleOrder(id);
        return ResponseEntity.ok(null);
    }
}
