package com.gui.payment_oauth_resourceServer.repositories;

import com.gui.payment_oauth_resourceServer.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long>{
}
