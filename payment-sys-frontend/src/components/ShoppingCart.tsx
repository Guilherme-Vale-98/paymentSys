import React from 'react'
import { Dropdown, Nav, Badge, Button } from 'react-bootstrap'
import { FaShoppingCart } from 'react-icons/fa'
import { Link, useNavigate } from 'react-router-dom'
import { Product } from '../types/Product';
import { useCart } from '../context/CartProvider';


type Props = {}

const ShoppingCart = (props: Props) => {
  const { cartItems, increaseQuantity, decreaseQuantity, clearCart, totalPrice } = useCart();

  const navigate = useNavigate();

  return (
    <Dropdown align="end">
    <Dropdown.Toggle as={Nav.Link} className="nav-link position-relative">
      <FaShoppingCart size={24} />
      {cartItems.length > 0 && (
        <Badge bg="danger" pill className="position-absolute top-0 start-100 translate-middle">
          {cartItems.reduce((total, item) => total + item.quantity, 0)}
        </Badge>
      )}
    </Dropdown.Toggle>
    <Dropdown.Menu style={{ minWidth: '300px' }}>
      {cartItems.length > 0 ? (
        <>
          {cartItems.map((item) => (
            <div key={item.product.id} className="d-flex p-2 justify-content-between align-items-center">
              <div>
                <strong>{item.product.name}</strong>
                <br />
                <small>Qty: {item.quantity}</small>
              </div>
              <div className="d-flex align-items-center">
                <Button
                  variant="outline-secondary"
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation()
                    decreaseQuantity(item.product.id)}
                  }
                  className="me-2"
                >
                  -
                </Button>
                <span>${(item.product.price * item.quantity).toFixed(2)}</span>
                <Button
                  variant="outline-secondary"
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation()
                    increaseQuantity(item.product.id)}
                  }
                  className="ms-2"
                >
                  +
                </Button>
              </div>
            </div>
          ))}
          <Dropdown.Divider />
          <div className="text-center">
              <Link className='btn btn-primary'  to="/checkout" >Checkout</Link>
          </div>
        </>
      ) : (
        <Dropdown.Item className="text-center">Your cart is empty</Dropdown.Item>
      )}
    </Dropdown.Menu>
  </Dropdown>
  )
}

export default ShoppingCart