import React from 'react'
import ProductList from '../components/ProductList'
import PaymentInfo from '../components/PaymentInfo'
import { Col, Container, Row } from 'react-bootstrap'
import { Elements } from '@stripe/react-stripe-js'
import { loadStripe } from '@stripe/stripe-js'
import { useCart } from '../context/CartProvider'

type Props = {}



const Checkout = (props: Props) => {
    const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_API_KEY);
    return (
        <Container>
            <Row>
                <Col md={6}>
                    <ProductList />
                </Col>
                <Col md={6}>
                    <Elements stripe={stripePromise}>
                        <PaymentInfo />
                    </Elements>
                </Col>
            </Row>
        </Container>
    )
}

export default Checkout