package com.gui.payment_oauth_resourceServer.controller;

import com.gui.payment_oauth_resourceServer.dto.OrderDTO;
import com.gui.payment_oauth_resourceServer.dto.OrderItemDTO;
import com.gui.payment_oauth_resourceServer.entities.*;
import com.gui.payment_oauth_resourceServer.repositories.OrderItemRepository;
import com.gui.payment_oauth_resourceServer.repositories.OrderRepository;
import com.gui.payment_oauth_resourceServer.repositories.ProductRepository;
import com.gui.payment_oauth_resourceServer.repositories.UserRepository;
import com.gui.payment_oauth_resourceServer.utils.CustomerUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
public class StripeController {

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/integrated")
    public String userInfo() {
        return "Hello authenticaed user";
    }
    @PostMapping("/checkout/integrated")
    @Transactional
    String integratedCheckout(@RequestBody OrderDTO orderDTO) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmail(jwt.getClaimAsString("email"));
        if (userOptional.isEmpty()) throw new RuntimeException("Invalid User");
        Customer customer = CustomerUtils.findOrCreateCustomer(userOptional.get().getEmail(),
                userOptional.get().getName());
        Long orderAmount = calculateOrderAmount(orderDTO.getItems()) * 100;


        Order order = new Order();
        order.setUser(userOptional.get());
        order.setTotalAmount(BigDecimal.valueOf(orderAmount).divide(BigDecimal.valueOf(100)));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(orderAmount)
                        .setCurrency("usd")
                        .setCustomer(customer.getId())
                        .putMetadata("orderId", String.valueOf(order.getId()))
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);



        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            Optional<Product> productOptional = productRepository.findById(itemDTO.getProductId());
            if (productOptional.isEmpty()) throw new RuntimeException("product not found id: " + itemDTO.getProductId());
            orderItem.setProduct(productOptional.get());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItemRepository.save(orderItem);
        }


        return paymentIntent.getClientSecret();
    }

    private Long calculateOrderAmount(List<OrderItemDTO> orderItemDTOS) {
        long total = 0L;
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Optional<Product> productOptional = productRepository.findById(orderItemDTO.getProductId());
            if (productOptional.isEmpty()) throw new RuntimeException("product not found id: " + orderItemDTO.getProductId());
            BigDecimal price = productOptional.get().getPrice();
            total += price.multiply(new BigDecimal(orderItemDTO.getQuantity())).longValue();
        }

        return (total);
    }
}