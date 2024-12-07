import React, { useEffect, useState } from 'react';
import { useUser } from '../context/UserProvider';
import { ListGroup } from 'react-bootstrap';
import { useStripe } from '@stripe/react-stripe-js';
import { useCart } from '../context/CartProvider';
import { useNavigate } from 'react-router-dom';
import { CardPaymentMethod, isCardPaymentMethod, PaymentMethod, UserPaymentMethod } from '../types/PaymentMethodInterface';

type Props = {};


/* const paymentMethods: PaymentMethod[] = [
    { id: "pm_card_visa", type: "Visa", last4: "1234", expiry: "12/25", cardholderName: "John Doe" },
    { id: "pm_card_mastercard", type: "MasterCard", last4: "5678", expiry: "11/24", cardholderName: "John Doe" },
];
 */
const PaymentInfo: React.FC<Props> = () => {
    const { cartItems, clearCart } = useCart();
    const [selectedPayment, setSelectedPayment] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [paymentMethods, setPaymentMethods] = useState<UserPaymentMethod[]>([]);
    const stripe = useStripe();
    const { user } = useUser();
    const navigate = useNavigate();

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))
        ?.split('=')[1];

    useEffect(() => {
        const getPaymentMethods = async () => {
            try {
                if (!token) {
                    throw new Error('Token not found');
                }

                const response = await fetch(import.meta.env.VITE_SERVER_BASE_URL + '/payment-methods', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    }
                });
                if (!response.ok) {
                    throw new Error('Failed to get payment methods');
                }
                const data = await response.json();
                console.log(data)
                setPaymentMethods(data)
            } catch (error) {
                console.error("Error fetching payment methods:", error);
            }
        };

        getPaymentMethods();
    }, []);


    const handlePaymentSelect = (method: UserPaymentMethod) => {
        if (isCardPaymentMethod(method)) {
            setSelectedPayment(method.cardIdentifier)
            method.last4Digits
        }
    };

    const handleConfirmPayment = async () => {
        if (!selectedPayment) {
            alert("Please select a payment method.");
            return;
        }

        setLoading(true);

        try {
            const response = await fetch(import.meta.env.VITE_SERVER_BASE_URL + "/checkout/integrated", {
                method: "POST",
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
                body: JSON.stringify({
                    items: cartItems.map(item => ({
                        productId: item.product.id,
                        quantity: item.quantity
                    })),
                    customerName: user?.name,
                    customerEmail: user?.email,
                    paymentMethodId: "pm_card_visa",
                })
            });

            const clientSecret = await response.text();
            console.log(clientSecret)
            if (!stripe) return;

            const result = await stripe.confirmCardPayment(clientSecret, {
                payment_method: selectedPayment,
            });

            if (result.error) {
                console.error("Payment failed:", result.error.message);
                alert("Payment failed. Please try again.");
            } else if (result.paymentIntent?.status === "succeeded") {
                alert("Payment successful!");
                clearCart();
                navigate("/");

            }
        } catch (error) {
            console.error("Error creating transaction:", error);
            alert("Failed to initiate payment. Please try again later.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h5>Select Payment Method</h5>
            <ListGroup>
                {paymentMethods.map((method) => {
                    if (isCardPaymentMethod(method)) {
                        const isSelected = selectedPayment === method.cardIdentifier;
                        return (
                            <ListGroup.Item
                                key={method.id}
                                onClick={() => handlePaymentSelect(method)}
                                className="d-flex justify-content-between align-items-center"
                                style={{
                                    cursor: 'pointer',
                                    border: isSelected ? '2px solid #007bff' : '1px solid #dee2e6',
                                    borderRadius: '4px',
                                }}
                            >
                                <span>{`•••• ${method.last4Digits}`}</span>
                                <span>{method.type}</span>
                            </ListGroup.Item>
                        );
                    }
                })}
            </ListGroup>
            <button
                className="w-full px-4 py-2 bg-green-500 text-black rounded-md mt-3 hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-500"
                disabled={!stripe || loading}
                onClick={handleConfirmPayment}
            >
                {loading ? "Processing..." : "Confirm Payment"}
            </button>
        </div>
    );
};

export default PaymentInfo;
