package com.gui.payment_oauth_resourceServer.controller;


import com.gui.payment_oauth_resourceServer.entities.Order;
import com.gui.payment_oauth_resourceServer.entities.OrderStatus;
import com.gui.payment_oauth_resourceServer.repositories.OrderRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StripeWebhookController {

    @Value("${stripe.webhookSecret}")
    private String endpointSecret;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("HERE");
        try {
            Event event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
                handlePaymentSuccess(paymentIntent);
            } else if ("payment_intent.payment_failed".equals(event.getType())) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
                handlePaymentFailure(paymentIntent);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Webhook Error");
        }
    }

    private void handlePaymentSuccess(PaymentIntent paymentIntent) {
        String orderId = paymentIntent.getMetadata().get("orderId");
        Optional<Order> optionalOrder = orderRepository.findById(Long.parseLong(orderId));
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
        }
    }

    private void handlePaymentFailure(PaymentIntent paymentIntent) {
        String orderId = paymentIntent.getMetadata().get("orderId");
        Optional<Order> optionalOrder = orderRepository.findById(Long.parseLong(orderId));
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
        }
    }
}