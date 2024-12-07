package com.gui.payment_oauth_client.entities;

import jakarta.persistence.Entity;

@Entity
public class PaypalPaymentMethod extends PaymentMethod {

    private String paypalEmail;

    public PaypalPaymentMethod() {}

    public PaypalPaymentMethod(String paypalEmail, User user, Boolean active) {
        super("PAYPAL", user, active);
        this.paypalEmail = paypalEmail;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }


}
