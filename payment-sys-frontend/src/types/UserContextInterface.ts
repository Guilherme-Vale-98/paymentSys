import { User } from "./UserInterface";
export interface UserContextType {
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>;
  isLoading: boolean;  // Add isLoading property
}