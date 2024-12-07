package com.gui.payment_oauth_client.services;

import com.gui.payment_oauth_client.entities.*;
import com.gui.payment_oauth_client.jwtUtils.JwtTokenUtil;
import com.gui.payment_oauth_client.repository.PaymentMethodRepository;
import com.gui.payment_oauth_client.repository.RoleRepository;
import com.gui.payment_oauth_client.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public User findUserByName(String name){
        Optional<User> userOptional = userRepository.findByName(name);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User Not Found");
        }
        return userOptional.get();
    }


    public User processOAuthPostLogin(String email, String name, Provider provider) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider(provider);
                    newUser.setEnabled(true);
                    return userRepository.save(newUser);
                });
        ensureTestCardForUser(user);
        return user;
    }


    public void processOidcUser(HttpServletResponse response, OidcUser oidcUser) throws IOException {
        OidcIdToken idToken = oidcUser.getIdToken();

        User user = processOAuthPostLogin(
                oidcUser.getAttribute("email"),
                oidcUser.getAttribute("name"),
                Provider.GOOGLE
        );

        Cookie jwtCookie = new Cookie("token", idToken.getTokenValue());
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(false);
        response.addCookie(jwtCookie);

        response.sendRedirect("http://localhost:5173");
    }


    public User processCustomLogin(String email, String password, HttpServletResponse response) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }


        String token = jwtTokenUtil.generateJwtToken(user.getEmail(), user.getName(), user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));


        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(false);
        response.addCookie(jwtCookie);

        return user;
    }

    public void ensureTestCardForUser(User user) {
        Optional<PaymentMethod> existingTestCard = user.getPaymentMethods().stream()
                .filter(paymentMethod -> "CARD".equals(paymentMethod.getPaymentMethodType()) &&
                        paymentMethod instanceof CardPaymentMethod)
                .findFirst();

        if (!existingTestCard.isPresent()) {
            createTestCardForUser(user);
        }
    }
    private void createTestCardForUser(User user) {
        CardPaymentMethod testCard = new CardPaymentMethod(
                "pm_card_visa",
                "Visa",
                "4242 4242 4242 4242",
                user.getName(),
                "12/25",
                user,
                true
        );

        user.addPaymentMethod(testCard);

        userRepository.save(user);
    }
}