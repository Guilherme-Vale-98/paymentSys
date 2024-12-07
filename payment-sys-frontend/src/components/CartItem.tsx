import React from 'react';

export interface ItemData {
  name: string;
  price: number;
  quantity: number;
  image: string;
  description: string;
  id: string;
}

interface CartItemProps {
  data?: ItemData;
  mode?: "subscription" | "checkout";
  onCancelled?: () => void;
}

const CartItem: React.FC<CartItemProps> = ({ 
  data = {
    name: "Sample Product",
    price: 99.99,
    quantity: 1,
    image: "/api/placeholder/200/200",
    description: "This is a sample product description that showcases how the cart item component looks with real content.",
    id: "sample-1"
  },
  mode = "checkout",
  onCancelled 
}) => {
  return (
    <div className="w-full max-w-xl rounded-lg border border-gray-200 bg-white shadow">
      <div className="flex flex-col sm:flex-row">
        <img
          src={data.image}
          alt={data.name}
          className="object-cover w-full sm:w-48 h-48 rounded-t-lg sm:rounded-l-lg sm:rounded-t-none"
        />
        <div className="flex flex-col flex-1 p-6">
          <div className="mb-4">
            <h3 className="text-xl font-bold text-gray-900">{data.name}</h3>
          </div>
          
          <div className="flex-grow">
            <div className="space-y-2">
              <p className="text-gray-600">{data.description}</p>
              {mode === "checkout" && (
                <p className="text-gray-600">Quantity: {data.quantity}</p>
              )}
            </div>
          </div>
          
          <div className="mt-4 space-y-2">
            <p className="text-blue-600 text-2xl">
              ${data.price.toFixed(2)}
            </p>
            {onCancelled && (
              <button 
                onClick={onCancelled}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                Cancel
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartItem;