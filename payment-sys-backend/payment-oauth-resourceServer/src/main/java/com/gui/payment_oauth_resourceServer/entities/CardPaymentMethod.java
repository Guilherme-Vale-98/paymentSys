package com.gui.payment_oauth_resourceServer.entities;

import com.gui.payment_oauth_resourceServer.entities.PaymentMethod;
import jakarta.persistence.Entity;


@Entity
public class CardPaymentMethod extends PaymentMethod {

    private String cardIdentifier;
    private String type;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;

    public CardPaymentMethod() {
    }

    public CardPaymentMethod(String cardIdentifier, String type, String cardNumber, String cardHolderName, String expirationDate, User user, Boolean active) {
        super("CARD", user, active);
        this.cardIdentifier = cardIdentifier;
        this.type = type;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }


}