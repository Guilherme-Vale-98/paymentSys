package com.gui.payment_oauth_resourceServer.controller;

import com.gui.payment_oauth_resourceServer.dto.CardPaymentMethodDTO;
import com.gui.payment_oauth_resourceServer.dto.PaypalPaymentMethodDTO;
import com.gui.payment_oauth_resourceServer.dto.PaymentMethodDTO;
import com.gui.payment_oauth_resourceServer.entities.CardPaymentMethod;
import com.gui.payment_oauth_resourceServer.entities.PaypalPaymentMethod;
import com.gui.payment_oauth_resourceServer.entities.User;
import com.gui.payment_oauth_resourceServer.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminEndpoint() {
        return "This is an admin-only endpoint";
    }


    @GetMapping("/api/userinfo")
    public String userInfo() {
        return "Hello authenticaed user";
    }

    @GetMapping("/test")
    public String test() {
        String test = "testint...";
        return test;
    }


    @GetMapping("/payment-methods")
    public ResponseEntity getUserPaymentMethods(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaimAsString("email");
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<PaymentMethodDTO> paymentMethodDTOs = getPaymentMethodDTOS(user);
            return new ResponseEntity<>(paymentMethodDTOs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private static List<PaymentMethodDTO> getPaymentMethodDTOS(User user) {
        return user.getPaymentMethods().stream()
                .map(paymentMethod -> {
                    switch (paymentMethod.getPaymentMethodType()) {
                        case "CARD":
                            CardPaymentMethod card = (CardPaymentMethod) paymentMethod;
                            return new CardPaymentMethodDTO(card.getId(),
                                    card.getCardIdentifier(),
                                    card.getType(),
                                    card.getCardNumber(),
                                    card.getCardHolderName(), card.getExpirationDate());
                        case "PAYPAL":
                            PaypalPaymentMethod paypal = (PaypalPaymentMethod) paymentMethod;
                            return new PaypalPaymentMethodDTO(paypal.getId(), paypal.getPaymentMethodType(), paypal.getPaypalEmail());
                        default:
                            throw new IllegalArgumentException("Unknown payment method type: " + paymentMethod.getPaymentMethodType());
                    }
                })
                .collect(Collectors.toList());
    }
}