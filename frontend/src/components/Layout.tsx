import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import NavMenu from "./navigation/NavMenu";
import { Button } from "@/components/ui/button";
import { Menu } from "lucide-react";

const Layout = () => {
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <div className="flex flex-col md:flex-row">
      {isMobile && (
        <div className="flex justify-between items-center p-4 bg-gray-800 text-white fixed w-full z-50">
          <h1 className="text-xl">EduHub</h1>
          <Button variant="ghost" onClick={() => setIsMenuOpen(!isMenuOpen)}>
            <Menu className="h-6 w-6" />
          </Button>
        </div>
      )}
      <aside
        className={`${
          isMobile
            ? isMenuOpen
              ? "fixed inset-0 z-40 w-full"
              : "hidden"
            : "fixed w-64"
        } md:min-h-screen bg-gray-800 text-white`}
      >
        <NavMenu />
      </aside>
      <main
        className={`flex-1 ${
          isMobile ? "mt-16 px-4" : "ml-64 px-4"
        } flex justify-center items-center`}
      >
        <div className="w-full max-w-4xl mx-auto">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default Layout;
