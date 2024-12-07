import React, { useEffect, useState } from "react";
import { Elements, PaymentElement, useElements, useStripe } from '@stripe/react-stripe-js';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import CartItem, { ItemData } from "../components/CartItem";

// Sample products for the artifact demo
const Products: ItemData[] = [
  {
    id: "1",
    name: "Sample Product",
    price: 99.99,
    quantity: 1,
    image: "/api/placeholder/200/200",
    description: "Sample product description"
  }
];

function IntegratedCheckout() {
  const [items] = useState<ItemData[]>(Products);
  const [transactionClientSecret, setTransactionClientSecret] = useState("");
  const [stripePromise, setStripePromise] = useState<Promise<Stripe | null> | null>(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  const onCustomerNameChange = (ev: React.ChangeEvent<HTMLInputElement>) => {
    setName(ev.target.value);
  };

  const onCustomerEmailChange = (ev: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(ev.target.value);
  };

  useEffect(() => {
    setStripePromise(loadStripe("pk_test_51Mfj5CBWgQV0mWdkArLnnyxfuahXWfSS3XKLAFH39qV7biWrV5NqNHC5cFvwuXKHtoxifAT7tqZbBDOjwOO5Nsfc00i7R0yVti"));
  }, []);

  const createTransactionSecret = () => {
    fetch(import.meta.env.VITE_SERVER_BASE_URL + "/checkout/integrated", {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        items: items.map(elem => ({ name: elem.name, id: elem.id, price: elem.price })),
        customerName: name,
        customerEmail: email,
      })
    })
      .then(r => r.text())
      .then(r => {
        setTransactionClientSecret(r);
      });
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="flex flex-col items-center space-y-24 max-w-4xl w-full">
        <h1 className="text-3xl font-bold">Integrated Checkout Example</h1>
        
        <div className="w-full space-y-6">
          {items.map(elem => (
            <CartItem key={elem.id} data={elem} mode="checkout" />
          ))}
        </div>

        <div className="w-full max-w-md space-y-4">
          <input
            type="text"
            className="w-full px-4 py-2 rounded-md border border-gray-300 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Customer Name"
            onChange={onCustomerNameChange}
            value={name}
          />
          <input
            type="email"
            className="w-full px-4 py-2 rounded-md border border-gray-300 bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Customer Email"
            onChange={onCustomerEmailChange}
            value={email}
          />
          <button
            onClick={createTransactionSecret}
            className="w-full px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            Initiate Payment
          </button>
        </div>

        {transactionClientSecret !== "" && (
          <Elements stripe={stripePromise} options={{ clientSecret: transactionClientSecret }}>
            <CheckoutForm />
          </Elements>
        )}
      </div>
    </div>
  );
}

const CheckoutForm = () => {
  const stripe = useStripe();
  const elements = useElements();

  const handleSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    
    if (!stripe || !elements) {
      return;
    }

    const result = await stripe.confirmPayment({
      elements,
      confirmParams: {
        return_url: "http://localhost:5173",
      },
    });

    if (result.error) {
      console.log(result.error.message);
    }
  };

  return (
    <div className="w-full max-w-md space-y-4">
      <PaymentElement />
      <button
        className="w-full px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed"
        disabled={!stripe}
        onClick={handleSubmit}
      >
        Pay
      </button>
    </div>
  );
};

export default IntegratedCheckout;