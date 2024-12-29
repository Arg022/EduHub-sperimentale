import { Link } from "react-router-dom";

export function Footer() {
  return (
    <footer className="bg-background border-t mt-auto">
      <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
          <div className="flex space-x-6">
            <Link
              to="/terms"
              className="text-sm text-muted-foreground hover:text-foreground"
            >
              Terms of Service
            </Link>
            <Link
              to="/privacy"
              className="text-sm text-muted-foreground hover:text-foreground"
            >
              Privacy Policy
            </Link>
          </div>
          <div className="text-sm text-muted-foreground">
            © {new Date().getFullYear()} YourCompany. All rights reserved.
          </div>
        </div>
      </div>
    </footer>
  );
}