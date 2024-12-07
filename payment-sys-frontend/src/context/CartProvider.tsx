import { createContext, ReactNode, useContext, useState } from "react";
import { CartContextInterface } from "../types/CartContextInterface";
import { CartItem } from "../types/CartItem";

import { Product } from "../types/Product";

const CartContext = createContext<CartContextInterface | undefined>(undefined);

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [cartItems, setCartItems] = useState<CartItem[]>([]);

    const addToCart = (product: Product) => {
        setCartItems((prevItems) => {
            const existingItem = prevItems.find((cartItem) =>
                 cartItem.product.id === product.id);
            if (existingItem) {
                return prevItems.map((cartItem) =>
                    cartItem.product.id === product.id
                        ? { ...cartItem, quantity: cartItem.quantity + 1 }
                        : cartItem
                );
            } else {
                return [...prevItems, { product, quantity: 1 }];
            }
        });
    };

    const increaseQuantity = (id: number) => {
        setCartItems((prevItems) =>
            prevItems.map((item) =>
                item.product.id === id ? { ...item, quantity: item.quantity + 1 } : item
            )
        );
    };

    const decreaseQuantity = (id: number) => {
        setCartItems((prevItems) =>
            prevItems
                .map((item) =>
                    item.product.id === id ? { ...item, quantity: item.quantity - 1 } : item
                )
                .filter((item) => item.quantity > 0)
        );
    };

    const clearCart = () => {
        setCartItems([]);
    };

    const totalPrice = () => {
        return cartItems.reduce((total, item) => total + item.product.price * item.quantity, 0)
    };


    return (
        <CartContext.Provider value={{ cartItems, addToCart, increaseQuantity, decreaseQuantity, clearCart, totalPrice }}>
            {children}
        </CartContext.Provider>
    );

}


export const useCart = (): CartContextInterface => {
    const context = useContext(CartContext);
    if (!context) {
        throw new Error('useCart must be used within a CartProvider');
    }
    return context;
};