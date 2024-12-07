package com.gui.payment_oauth_resourceServer.dto;
public class CardPaymentMethodDTO extends PaymentMethodDTO {
    private Long id;
    private String cardIdentifier;
    private String type;
    private String last4Digits;
    private String cardHolderName;
    private String expirationDate;

    private String Type;
    public CardPaymentMethodDTO(Long id, String cardIdentifier, String type,
                                String last4Digits, String cardHolderName, String expirationDate) {
        super("CARD");
        this.id = id;
        this.cardIdentifier = cardIdentifier;
        this.type = type;
        this.last4Digits = last4Digits.substring(last4Digits.length() - 4);
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }

    public Long getId() { return id; }
    public String getCardIdentifier() { return cardIdentifier; }
    public String getType() { return type; }
    public String getLast4Digits() { return last4Digits; }
    public String getCardHolderName() { return cardHolderName; }
    public String getExpirationDate() { return expirationDate; }

    public void setId(Long id) { this.id = id; }
    public void setCardIdentifier(String cardIdentifier) { this.cardIdentifier = cardIdentifier; }
    public void setType(String type) { this.type = type; }
    public void setLast4Digits(String last4Digits) { this.last4Digits = last4Digits.substring(last4Digits.length() - 4); }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
}