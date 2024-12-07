package com.gui.payment_oauth_resourceServer.repositories;

import com.gui.payment_oauth_resourceServer.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
