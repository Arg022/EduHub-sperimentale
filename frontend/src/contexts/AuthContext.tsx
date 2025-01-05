import { IUser } from "@/interfaces/interfaces";
import axios from "axios";
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { useLocation, useNavigate } from "react-router-dom";

const STORAGE_URL = import.meta.env.VITE_STORAGE_URL;

export const api = axios.create({
  baseURL: `${STORAGE_URL}/api`,
});

interface IAuthContextProps {
  user?: IUser;
  setAsLogged: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<IAuthContextProps | null>(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<IUser>();

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const token = localStorage.getItem("ACCESS_TOKEN");
    if (!token) {
      if (location.pathname === "/auth/register") {
        return;
      }
      navigate("/auth/login");
      return;
    }
    getUser(token);
  }, []);

  const getUser = (token: string, isLogin: boolean = false) => {
    if (token) {
      axios
        .get(`${STORAGE_URL}/api/v1/user`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then(({ data: authUser }) => {
          api.interceptors.request.use((config) => {
            config.headers.Authorization = `Bearer ${token}`;
            return config;
          });
          setUser(authUser);
          if (isLogin) {
            navigate("/");
          }
        })
        .catch((err) => {
          navigate("/auth/login");
          console.log(err);
        });
    }
  };

  const setAsLogged = (token: string) => {
    // setto il valore del token in localStorage
    localStorage.setItem("ACCESS_TOKEN", token);
    getUser(token, true);
  };

  const logout = () => {
    localStorage.removeItem("ACCESS_TOKEN");
    setUser(undefined);
    navigate("/auth/login");
  };

  return (
    <AuthContext.Provider value={{ user, setAsLogged, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
