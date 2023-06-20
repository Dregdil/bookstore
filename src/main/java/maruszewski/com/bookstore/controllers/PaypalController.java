package maruszewski.com.bookstore.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import maruszewski.com.bookstore.dtos.OrderStatusDto;
import maruszewski.com.bookstore.models.Orders;
import maruszewski.com.bookstore.repository.OrderRepository;
import maruszewski.com.bookstore.service.OrderService;
import maruszewski.com.bookstore.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;

@RestController
@SecurityRequirement(name="security_auth")
@RequiredArgsConstructor
@RequestMapping(value="/paypal", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
@ExposesResourceFor(Orders.class)
public class PaypalController {

    @Autowired
    private final PaypalService service;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/pay")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @orderService.getSingleOrder(#orderId).user.login == authentication.name")
    public String payment(@RequestBody Long orderId) {
        try {
            Orders order = orderRepository.findById(orderId).orElseThrow();
            Payment payment = service.createPayment(order.getCost(), "PLN", "paypal",
                    "sale", orderId.toString(), "http://localhost:8080/paypal/" + CANCEL_URL,
                    "http://localhost:8080/paypal/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "PAYMENT CANCELED";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                OrderStatusDto orderStatusDto = new OrderStatusDto();
                orderStatusDto.setStatus("PAYED");
                Long orderId = Long.parseLong(payment.getTransactions().get(0).getDescription());
                orderService.updateSingleOrder(orderId, orderStatusDto);
                return "PAYMENT SUCCESS";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

}