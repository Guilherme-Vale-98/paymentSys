
export interface PaymentMethod {
    id: string;
    paymentMethodType: "CARD" | "PAYPAL";
    active: boolean;
  }
  
  export interface CardPaymentMethod extends PaymentMethod {
    cardIdentifier: string;
    type: string;
    last4Digits: string;
    cardHolderName: string;
    expirationDate: string;
  }
  
  export interface PaypalPaymentMethod extends PaymentMethod {
    email: string;
  }
  
  export type UserPaymentMethod = CardPaymentMethod | PaypalPaymentMethod;

  export function isCardPaymentMethod(method: UserPaymentMethod): method is CardPaymentMethod {
    return method.paymentMethodType === "CARD";
  }