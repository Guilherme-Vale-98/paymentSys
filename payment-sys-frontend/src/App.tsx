import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import { useUser } from './context/UserProvider';
import { jwtDecode } from 'jwt-decode';
import { User } from './types/UserInterface';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import IntegratedCheckout from './pages/IntegratedCheckout';
import Checkout from './pages/Checkout';
import RegisterPage from './pages/RegisterPage';




const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<Layout />}>
      <Route index element={<Home />} />
      <Route path="profile" element={
        <ProtectedRoute>
          <Profile />
        </ProtectedRoute>
      } />
      <Route path="login" element={<LoginPage />} />
      <Route path="checkout" element={<ProtectedRoute><Checkout /> </ProtectedRoute>}/>
      <Route path="integrated-checkout" element={<IntegratedCheckout />} />
      <Route path="register" element={<RegisterPage />} />
    </Route>
  )
);

function App() {

  return (
    <>
      <RouterProvider router={router} />
    </>

  )
}

export default App
