import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import NavMenu from "./navigation/NavMenu";

const Layout = () => {
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className="flex flex-col md:flex-row">
      <aside className={`${isMobile ? 'w-full' : 'w-64'} md:min-h-screen`}>
        <NavMenu />
      </aside>
      <main className={`flex-1 ${isMobile ? 'mt-16 px-4' : 'ml-64'}`}>
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;