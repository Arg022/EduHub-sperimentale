import { Route, Routes } from "react-router-dom";
import UserForm from "./components/UserForm";
import Layout from "./components/Layout";
import UserDetails from "./components/UserDetails";
import { StoreProvider } from "./contexts/StoreContext";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import UsersList from "./pages/UsersList";
import { AuthProvider } from "./contexts/AuthContext";
import UploadFile from "./pages/UploadFile";
import AuthLayout from "./components/AuthLayout";

function App() {
  return (
    <AuthProvider>
      <StoreProvider>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<UsersList />} />
            <Route path="add-user" />
            <Route path="edit-user" element={<UserForm title="Edit user" />} />
            <Route path="users">
              <Route path=":userId" element={<UserDetails />} />
            </Route>
            <Route path="upload-file" element={<UploadFile />} />
            <Route path="*" element={<>Page not found!</>} />
          </Route>
          <Route path="/auth" element={<AuthLayout />}>
            <Route path="login" element={<Login />}></Route>
            <Route path="register" element={<Register />}></Route>
          </Route>
        </Routes>
      </StoreProvider>
    </AuthProvider>
  );
}

export default App;
