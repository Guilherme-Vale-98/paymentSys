package com.gui.payment_oauth_resourceServer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract  class  PaymentMethodDTO{
    @JsonProperty
    private String paymentMethodType;

    public PaymentMethodDTO(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }
}