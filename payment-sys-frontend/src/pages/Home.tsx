import { jwtDecode } from 'jwt-decode';
import React, { useEffect, useState } from 'react'
import ProductCard from '../components/ProductCard';
import { Container, Row, Col } from 'react-bootstrap';
import { Product } from '../types/Product';

type Props = {}


const Home = (props: Props) => {
  const [products , setProducts] = useState<Product[]>([]);
  
  useEffect(() => {
    const fetchProducts = async () => {

      try {
        const response = await fetch(import.meta.env.VITE_SERVER_BASE_URL + '/products');
        
        if (!response.ok) {
          throw new Error('Failed to fetch products');
        }
        const data: Product[] = await response.json();
        console.log(data)
        setProducts(data); 
      } catch (error) {
        console.error("Error fetching products:", error);
      }
    };
    fetchProducts();
  }, []);


  return (
    <div>
      <Container>
        <Row>
          {products.map((product, index) => (
            <Col key={index} xs={12} sm={6} md={4} lg={3} className="mb-4">
              <ProductCard
                key={product.id}
                product={product}
              />
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  )
}

export default Home