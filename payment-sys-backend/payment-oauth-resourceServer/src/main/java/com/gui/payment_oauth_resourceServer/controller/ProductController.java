package com.gui.payment_oauth_resourceServer.controller;

import com.gui.payment_oauth_resourceServer.entities.Product;
import com.gui.payment_oauth_resourceServer.repositories.ProductRepository;
import com.gui.payment_oauth_resourceServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;


    @GetMapping("/products")
    public ResponseEntity getAllProducts(){
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }
}
