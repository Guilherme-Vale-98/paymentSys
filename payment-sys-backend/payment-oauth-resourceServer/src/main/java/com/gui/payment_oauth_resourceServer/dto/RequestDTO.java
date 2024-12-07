package com.gui.payment_oauth_resourceServer.dto;

import com.gui.payment_oauth_resourceServer.entities.Product;

import java.util.Currency;

public class RequestDTO {
    Product[] items;
    String customerName;
    String customerEmail;

    public Product[] getItems() {
        return items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

}