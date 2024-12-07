import { Link, Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import { products } from "../types/Product";



const Layout = () => {
    return (
        <main>
            <Navbar />
            <Outlet />
        </main>
    );
};

export default Layout