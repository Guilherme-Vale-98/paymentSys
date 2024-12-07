import { CartItem } from "./CartItem";
import { Product } from "./Product";

export interface CartContextInterface {
    cartItems: CartItem[];
    addToCart: (product: Product) => void;
    increaseQuantity: (id: number) => void;
    decreaseQuantity: (id: number) => void;
    totalPrice: () => number;
    clearCart: () => void;
  }