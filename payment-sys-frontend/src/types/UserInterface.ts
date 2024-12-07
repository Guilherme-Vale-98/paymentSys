import { PaymentMethod, UserPaymentMethod } from "./PaymentMethodInterface";

export interface User {
  name: string;
  email: string;
  paymentMethods: UserPaymentMethod[] | null;
}
