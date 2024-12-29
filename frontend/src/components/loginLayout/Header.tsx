import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

export function Header() {
  return (
    <header className="flex items-center justify-between px-6 py-4 bg-background border-b">
      <div className="flex items-center space-x-4">
        <Link to="/" className="text-2xl font-bold">
          YourLogo
        </Link>
        <nav className="hidden md:flex space-x-4">
          <Link to="/" className="text-sm font-medium hover:underline">
            Home
          </Link>
          <Link to="/about" className="text-sm font-medium hover:underline">
            About
          </Link>
          <Link to="/contact" className="text-sm font-medium hover:underline">
            Contact
          </Link>
        </nav>
      </div>
      <Button className="sm:hidden" variant="outline" size="sm">
        <Link to="/login">Log In</Link>
      </Button>
    </header>
  );
}
