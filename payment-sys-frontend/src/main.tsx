import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import App from './App.tsx'
import UserProvider from './context/UserProvider.tsx'
import { CartProvider } from './context/CartProvider.tsx'

createRoot(document.getElementById('root')!).render(

  <StrictMode>
    <UserProvider>
      <CartProvider>
        <App />
      </CartProvider>
    </UserProvider>
  </StrictMode>,
)
