import { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { Button } from "@/components/ui/button";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import {
  Home,
  BookOpen,
  FileText,
  GraduationCap,
  UserCheck,
  BarChart2,
  Bell,
  ChevronRight,
  PlusCircle,
  Menu,
  X,
  LogOut,
  Calendar,
  Clock,
  Book,
} from "lucide-react";

interface MenuItem {
  label: string;
  path: string | ((role: string) => string);
  roles: Array<"STUDENT" | "TEACHER" | "ADMIN">;
  icon: React.ReactNode;
  subItems?: MenuItem[];
}

const menuItems: MenuItem[] = [
  {
    label: "Dashboard",
    path: (role) => `/dashboard/${role.toLowerCase()}`,
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <Home className="h-4 w-4" />,
  },
  {
    label: "Corsi",
    path: "/courses",
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <BookOpen className="h-4 w-4" />,
    subItems: [
      {
        label: "Tutti i corsi",
        path: "/courses",
        roles: ["STUDENT", "TEACHER", "ADMIN"],
        icon: <BookOpen className="h-4 w-4" />,
      },
      {
        label: "Crea Corso",
        path: "/courses/create",
        roles: ["ADMIN"],
        icon: <PlusCircle className="h-4 w-4" />,
      },
    ],
  },
  {
    label: "Materie",
    path: "/subjects",
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <Book className="h-4 w-4" />,
  },
  {
    label: "Materiali",
    path: "/materials",
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <FileText className="h-4 w-4" />,
  },
  {
    label: "Quiz",
    path: "/quiz",
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <GraduationCap className="h-4 w-4" />,
    subItems: [
      {
        label: "Elenco Quiz",
        path: "/quiz",
        roles: ["STUDENT", "TEACHER", "ADMIN"],
        icon: <FileText className="h-4 w-4" />,
      },
      {
        label: "Crea Quiz",
        path: "/quiz/create",
        roles: ["TEACHER", "ADMIN"],
        icon: <PlusCircle className="h-4 w-4" />,
      },
    ],
  },
  {
    label: "Lezioni",
    path: "/lessons",
    roles: ["STUDENT", "TEACHER", "ADMIN"],
    icon: <Calendar className="h-4 w-4" />,
    subItems: [
      {
        label: "Metti Presenze",
        path: "/lessons",
        roles: ["TEACHER", "ADMIN"],
        icon: <Calendar className="h-4 w-4" />,
      },
      {
        label: "Visualizza le Presenze",
        path: "/attendance/report",
        roles: ["STUDENT", "TEACHER", "ADMIN"],
        icon: <Clock className="h-4 w-4" />,
      },
    ],
  },
  {
    label: "Statistiche",
    path: "/statistics",
    roles: ["TEACHER", "ADMIN", "STUDENT"],
    icon: <BarChart2 className="h-4 w-4" />,
  },
  {
    label: "Notifiche",
    path: "/notifications",
    roles: ["TEACHER", "ADMIN"],
    icon: <Bell className="h-4 w-4" />,
    subItems: [
      {
        label: "Crea Notifica",
        path: "/notifications/create",
        roles: ["TEACHER", "ADMIN"],
        icon: <PlusCircle className="h-4 w-4" />,
      },
    ],
  },
];

export default function NavMenu() {
  const { user, logout } = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 768);
      if (window.innerWidth >= 768) {
        setIsOpen(true);
      }
    };

    window.addEventListener("resize", handleResize);
    handleResize();
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  if (!user) {
    return null;
  }

  const authorizedItems = menuItems.filter((item) =>
    item.roles.includes(user.role)
  );

  return (
    <nav
      className={`bg-slate-800 text-white ${
        isMobile
          ? "fixed top-0 left-0 right-0 z-50"
          : "h-screen w-64 fixed left-0 top-0"
      }`}
    >
      <div
        className={`${
          isMobile ? "container mx-auto px-4" : "h-full flex flex-col"
        }`}
      >
        {isMobile && (
          <div className="flex justify-between items-center h-16">
            <span className="text-xl font-bold">Menu</span>
            <Button
              variant="ghost"
              onClick={() => setIsOpen(!isOpen)}
              aria-label={isOpen ? "Chiudi menu" : "Apri menu"}
            >
              {isOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </Button>
          </div>
        )}
        <div
          className={`${
            isMobile
              ? isOpen
                ? "block"
                : "hidden"
              : "flex-grow overflow-y-auto"
          } space-y-2 py-4 flex flex-col`}
        >
          {authorizedItems.map((item) => (
            <div
              key={
                typeof item.path === "function"
                  ? item.path(user.role)
                  : item.path
              }
            >
              {item.subItems ? (
                <Collapsible>
                  <CollapsibleTrigger asChild>
                    <Button
                      variant="ghost"
                      className="w-full justify-start text-white hover:bg-slate-700"
                    >
                      {item.icon}
                      <span className="ml-3">{item.label}</span>
                      <ChevronRight className="ml-auto h-4 w-4" />
                    </Button>
                  </CollapsibleTrigger>
                  <CollapsibleContent className="pl-6 pt-2">
                    {item.subItems
                      .filter((subItem) => subItem.roles.includes(user.role))
                      .map((subItem) => (
                        <NavLink
                          key={
                            typeof subItem.path === "function"
                              ? subItem.path(user.role)
                              : subItem.path
                          }
                          to={
                            typeof subItem.path === "function"
                              ? subItem.path(user.role)
                              : subItem.path
                          }
                          className={({ isActive }) =>
                            `flex items-center p-2 rounded-md ${
                              isActive ? "bg-slate-600" : "hover:bg-slate-700"
                            }`
                          }
                          onClick={() => isMobile && setIsOpen(false)}
                        >
                          {subItem.icon}
                          <span className="ml-3">{subItem.label}</span>
                        </NavLink>
                      ))}
                  </CollapsibleContent>
                </Collapsible>
              ) : (
                <NavLink
                  to={
                    typeof item.path === "function"
                      ? item.path(user.role)
                      : item.path
                  }
                  className={({ isActive }) =>
                    `flex items-center p-2 rounded-md ${
                      isActive ? "bg-slate-600" : "hover:bg-slate-700"
                    }`
                  }
                  onClick={() => isMobile && setIsOpen(false)}
                >
                  {item.icon}
                  <span className="ml-3">{item.label}</span>
                </NavLink>
              )}
            </div>
          ))}
          <Button
            variant="ghost"
            className="w-full justify-start text-white hover:bg-slate-700 mt-auto"
            onClick={logout}
          >
            <LogOut className="h-4 w-4" />
            <span className="ml-3">Logout</span>
          </Button>
        </div>
      </div>
    </nav>
  );
}
