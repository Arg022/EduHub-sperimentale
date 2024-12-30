import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import { AuthProvider } from "./contexts/AuthContext";
import AuthLayout from "./components/AuthLayout";

import { StudentDashboard } from "./pages/dashboard/StudentDashboard";
import { TeacherDashboard } from "./pages/dashboard/TeacherDashboard";
import { AdminDashboard } from "./pages/dashboard/AdminDashboard";

function App() {
  return (
    <Routes>
      <Route path="/auth" element={<AuthLayout />}>
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />
      </Route>
      <Route path="student" element={<StudentDashboard />} />
      <Route path="teacher" element={<TeacherDashboard />} />
      <Route path="admin" element={<AdminDashboard />} />
      <Route path="unauthorized" />
      <Route path="*" />
    </Routes>
  );
}

export default App;
