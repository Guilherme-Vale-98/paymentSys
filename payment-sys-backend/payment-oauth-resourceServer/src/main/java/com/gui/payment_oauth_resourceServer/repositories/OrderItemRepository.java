package com.gui.payment_oauth_resourceServer.repositories;

import com.gui.payment_oauth_resourceServer.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
