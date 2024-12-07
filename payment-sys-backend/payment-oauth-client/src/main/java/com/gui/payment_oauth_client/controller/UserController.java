package com.gui.payment_oauth_client.controller;


import com.gui.payment_oauth_client.auth.LoginRequest;
import com.gui.payment_oauth_client.entities.User;
import com.gui.payment_oauth_client.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserController {


    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    UserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;


    @GetMapping("/hello")
    public String Hello() {
        return "Hello";
    }

    @GetMapping("/test")
    public String test() {
        return "Hello auth user";
    }
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            return ResponseEntity.ok("Token is valid");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        User user = userService.processCustomLogin(loginRequest.getEmail(), loginRequest.getPassword(), response);
        userService.ensureTestCardForUser(user);
        return ResponseEntity.ok().body(Map.of("message", "Login successful"));
    }


    @GetMapping("/debug")
    public String debugUser(Authentication authentication) {
        if (authentication == null) {
            return "No authentication found";
        }
        var principal = (OidcUser) authentication.getPrincipal();
        StringBuilder debug = new StringBuilder();
        debug.append("User: ").append(authentication.getName())
                .append("\nAuthorities: ").append(authentication.getAuthorities())
                .append("\nDetails: ").append(authentication.getDetails())
                .append("\nPrincipal: ").append(authentication.getPrincipal())
                .append("\nAuthentication Class: ").append(authentication.getClass().getName());

        return debug.toString();
    }
}
