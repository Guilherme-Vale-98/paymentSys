import React from 'react'
import { Product } from '../types/Product';
import { useCart } from '../context/CartProvider';

type Props = {
  product: Product
}


const ProductCard = ({product}: Props) => {
    const {addToCart} = useCart();

    return (
      <div className="card shadow-sm" style={{ width: '18rem', height: '25rem' }}>
        <div className="card-img-wrapper" style={{ height: '190px', overflow: 'hidden' }}>
          <img 
            src={`data:image/png;base64,${product.image}`} 
            className="card-img-top" 
            alt={product.name} 
            style={{ width: '100%', height: '100%', objectFit: 'fill' }} 
          />
        </div>
        <div className="card-body d-flex flex-column">
          <h5 className="card-title" style={{ fontSize: '1rem' }}>{product.name}</h5>
          <p className="card-text" style={{ flexGrow: 1, fontSize: '0.9rem' }}>{product.description}</p>
          <div className="d-flex justify-content-between align-items-center mt-auto">
            <span className="text-primary">${product.price.toFixed(2)}</span>
            <button className="btn btn-primary btn-sm" 
            onClick={() => addToCart(product)}>Adicionar ao carrinho</button>
          </div>
        </div>
      </div>
    );
  };

export default ProductCard