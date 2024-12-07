import React from 'react';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import BNavbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Offcanvas from 'react-bootstrap/Offcanvas';
import { useUser } from '../context/UserProvider';
import { Link } from 'react-router-dom';
import { FaShoppingCart } from 'react-icons/fa';
import Badge from 'react-bootstrap/Badge';
import Dropdown from 'react-bootstrap/Dropdown';
import { Product } from '../types/Product';
import ShoppingCart from './ShoppingCart';


type Props = {

};

const Navbar = (props: Props) => {
  const { user } = useUser();
  return (
    <>
      {['sm'].map((expand) => (
        <BNavbar key={expand} expand={expand} className="bg-body-tertiary py-3 px-5 mb-3">
          <Container fluid>
            <BNavbar.Brand as={Link} to={'/'}>MyProductStore</BNavbar.Brand>
            <BNavbar.Toggle aria-controls={`offcanvasNavbar-expand-${expand}`} />
            <BNavbar.Offcanvas
              id={`offcanvasNavbar-expand-${expand}`}
              aria-labelledby={`offcanvasNavbarLabel-expand-${expand}`}
              placement="end"
            >
              <Offcanvas.Header closeButton>
                <Offcanvas.Title id={`offcanvasNavbarLabel-expand-${expand}`}>
                  Offcanvas
                </Offcanvas.Title>
              </Offcanvas.Header>
              <Offcanvas.Body>
                <Nav className="justify-content-end flex-grow-1 pe-3">
                  <Link className="nav-link" to="/">Home</Link>
                  {user ? (
                    <>
                      <Link className="nav-link" to="/profile">Profile</Link>
                      <Link className="nav-link" to="http://localhost:8080/api/logout">Logout</Link>
                    </>
                  ) : (
                    <>
                      <Link className="nav-link" to="/login">Login</Link>
                      <Link className="nav-link" to="/register">Sign Up</Link>
                    </>
                  )}                  
                  {/* Cart Dropdown */}
                  <ShoppingCart/>
                </Nav>
              </Offcanvas.Body>
            </BNavbar.Offcanvas>
          </Container>
        </BNavbar>
      ))}
    </>
  );
};

export default Navbar;
