import { Outlet } from "react-router-dom";
import { Header } from "@/components/loginLayout/Header"
import { Footer } from "./loginLayout/Footer";

const AuthLayout = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
};

export default AuthLayout;
