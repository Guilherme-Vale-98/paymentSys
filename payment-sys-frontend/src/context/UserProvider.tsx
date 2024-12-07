import React, { createContext, ReactNode, useContext, useEffect, useState } from 'react'
import { UserContextType } from '../types/UserContextInterface';
import { User } from '../types/UserInterface';
import { jwtDecode } from 'jwt-decode';

type Props = {}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface jwtPayload {
  name: string
  email: string
  exp: number
}

const UserProvider: React.FC<{ children: ReactNode }> = ({ children })  => {
  const [user, setUser] = useState<User| null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const token = document.cookie.split('; ').find(row => row.startsWith('token='));
  
  useEffect(() => {
    const initializeUser = async () => {
      console.log(token)
      if (token && token.length > 6) {
        const decoded = jwtDecode<jwtPayload>(token);
        const isTokenExpired = decoded.exp * 1000 < Date.now();
        if(isTokenExpired) {
          setUser(null)
          document.cookie="token=; Max-Age=0";
          setIsLoading(false); 
          return
        }
        setUser({ email: decoded.email, name: decoded.name , paymentMethods: null});
      }
      setIsLoading(false);
    };
    initializeUser();
  }, [token]);
  return (
    <UserContext.Provider value={{ user, setUser, isLoading }}>
      {isLoading ? <div>Loading...</div> : children}
    </UserContext.Provider>
  );
};

export default UserProvider;

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};