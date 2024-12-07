package com.gui.payment_oauth_client.repository;

import com.gui.payment_oauth_client.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
