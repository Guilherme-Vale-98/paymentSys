package com.gui.payment_oauth_resourceServer.dto;
public class PaypalPaymentMethodDTO extends PaymentMethodDTO {
    private Long id;
    private String paymentMethodType;
    private String paypalEmail;

    public PaypalPaymentMethodDTO(Long id, String paymentMethodType, String paypalEmail) {
        super("Paypal");
        this.id = id;
        this.paymentMethodType = paymentMethodType;
        this.paypalEmail = paypalEmail;
    }

    public Long getId() { return id; }
    public String getPaymentMethodType() { return paymentMethodType; }
    public String getPaypalEmail() { return paypalEmail; }
}