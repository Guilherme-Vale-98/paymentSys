package com.gui.payment_oauth_resourceServer.entities;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethodType;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    private Boolean active;


    public PaymentMethod() {}

    public PaymentMethod(String paymentMethodType, User user, Boolean active) {
        this.paymentMethodType = paymentMethodType;
        this.user = user;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}