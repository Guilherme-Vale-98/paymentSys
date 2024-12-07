import React, { useEffect, useState } from 'react';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import { FaGoogle, FaFacebook, FaTwitter } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../context/UserProvider';

const LoginPage = () => {
    const { user } = useUser();
    const [isLoading, setLoading] = useState(true);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        if (user) {
            navigate("/")
        }
        setLoading(false)
    }, [])


    const handleLogin = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();
        console.log('handleLogin triggered');
        if (!email || !password) {
            setError('Please fill in both fields');
            return;
        }
        setError('');
        console.log({ email, password })
        const response = await fetch('http://localhost:8080/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password }),
            credentials: "include"
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to log in');
        }

        const data = await response.json();

        console.log('Login successful:', data);
        window.location.href = "http://localhost:5173";
    };

    const handleSocialLogin = (platform: any) => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";

    };
    if (isLoading) return <div>Loading...</div>

    return (
        <Container className="d-flex justify-content-center align-items-center">
            <Row className="w-100">
                <Col md={6} lg={4} className="mx-auto">
                    <h3 className="text-center mb-4">Login</h3>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleLogin}>
                        <Form.Group controlId="formEmail" className="mb-3">
                            <Form.Label>Email address</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Enter email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="formPassword" className="mb-3">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100 mb-3">
                            Login
                        </Button>
                    </Form>
                    <h3 className="text-center mb-4">Login with your socials</h3>
                    <hr></hr>
                    <div className="d-flex justify-content-center">
                        <Button
                            variant="outline-danger"
                            className="me-2"
                            onClick={() => handleSocialLogin('Google')}
                        >
                            <FaGoogle /> Google
                        </Button>
                        <Button
                            variant="outline-primary"
                            className="me-2"
                            onClick={() => handleSocialLogin('Facebook')}
                        >
                            <FaFacebook /> Facebook
                        </Button>
                        <Button
                            variant="outline-info"
                            onClick={() => handleSocialLogin('Twitter')}
                        >
                            <FaTwitter /> Twitter
                        </Button>
                    </div>
                </Col>
            </Row>
        </Container>
    );
};

export default LoginPage;