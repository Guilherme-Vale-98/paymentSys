import React from 'react'
import { Product } from '../types/Product'
import { ListGroup } from 'react-bootstrap'
import { useCart } from '../context/CartProvider'

type Props = {
}

const ProductList = (props: Props) => {
  const { cartItems, totalPrice } = useCart();

  return (
    <ListGroup>
      {cartItems.map((item) => (
        <ListGroup.Item key={item.product.name} className="d-flex justify-content-between align-items-center">
          <span>{item.product.name} </span>
          <div>
            <span>Qty: {(item.quantity)} </span>
            <span> Price: ${(item.product.price * item.quantity).toFixed(2)}</span>
          </div>
        </ListGroup.Item>
      ))}
      <ListGroup.Item className='d-flex justify-content-between'>
        <span>Total</span>
        <span>${totalPrice().toFixed(2)}</span>
      </ListGroup.Item>
    </ListGroup>
  )
}

export default ProductList