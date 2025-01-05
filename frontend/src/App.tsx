import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import { AuthProvider } from "./contexts/AuthContext";
import AuthLayout from "./components/AuthLayout";
import { AppProvider } from "./contexts/AppConetxt";
import ProtectedRoute from "./components/ProtectedRoute";
import { StudentDashboard } from "./pages/dashboard/StudentDashboard";
import { TeacherDashboard } from "./pages/dashboard/TeacherDashboard";
import { AdminDashboard } from "./pages/dashboard/AdminDashboard";
import CoursesList from "./pages/courses/CoursesList";
import CourseDetails from "./pages/courses/CourseDetails";
import CourseEdit from "./pages/courses/CourseEdit";
import MaterialsList from "./pages/materials/MaterialsList";
import MaterialUpload from "./pages/materials/MaterialUpoad";
import AttendanceRecord from "./pages/Attendance/AttendanceRecord";
import AttendanceReport from "./pages/Attendance/AttendanceReport";
import CourseCreate from "./pages/courses/CourseCreate";
import QuizList from "./pages/quiz/QuizList";
import QuizCreate from "./pages/quiz/QuizCreate";
import QuizAttempt from "./pages/quiz/QuizAttempt";
import QuizResults from "./pages/quiz/QuizResults";
import StudentStats from "./pages/statistics/StudentStats";
import CourseStats from "./pages/statistics/CourseStats";
import NotificationCenter from "./pages/notifications/NotificationCenter";
import CreateNotification from "./pages/notifications/CreateNotification";
import LessonsList from "./pages/lessons/LessonsList";
import LessonDetails from "./pages/lessons/LessonDetails";
import TodayLessons from "./pages/lessons/TodayLessons";
import SubjectList from "./pages/subjects/SubjectList";
import { Toaster } from "./components/ui/toaster";

function App() {
  return (
    <AuthProvider>
      <AppProvider>
        <Toaster />
        <Routes>
          {/* Auth Routes - Pubbliche */}
          <Route path="/auth" element={<AuthLayout />}>
            <Route path="login" element={<Login />} />
            <Route path="register" element={<Register />} />
          </Route>

          {/* Protected Routes */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            {/* Dashboard Routes based on role */}
            <Route path="dashboard">
              <Route
                path="student"
                element={
                  <ProtectedRoute roles={["STUDENT"]}>
                    <StudentDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="teacher"
                element={
                  <ProtectedRoute roles={["TEACHER"]}>
                    <TeacherDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="admin"
                element={
                  <ProtectedRoute roles={["ADMIN"]}>
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Courses */}
            <Route path="courses">
              <Route index element={<CoursesList />} />
              <Route path=":courseId">
                <Route index element={<CourseDetails />} />
                <Route
                  path="edit"
                  element={
                    <ProtectedRoute roles={["TEACHER", "ADMIN"]}>
                      <CourseEdit />
                    </ProtectedRoute>
                  }
                />
                <Route path="materials">
                  <Route index element={<MaterialsList />} />
                  <Route
                    path="upload"
                    element={
                      <ProtectedRoute roles={["TEACHER"]}>
                        <MaterialUpload />
                      </ProtectedRoute>
                    }
                  />
                </Route>
                <Route path="attendance">
                  <Route
                    path="record"
                    element={
                      <ProtectedRoute roles={["TEACHER"]}>
                        <AttendanceRecord />
                      </ProtectedRoute>
                    }
                  />
                  <Route path="report" element={<AttendanceReport />} />
                </Route>
              </Route>
              <Route
                path="create"
                element={
                  <ProtectedRoute roles={["ADMIN"]}>
                    <CourseCreate />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Quiz System */}
            <Route path="quiz">
              <Route index element={<QuizList />} />
              <Route
                path="create"
                element={
                  <ProtectedRoute roles={["TEACHER"]}>
                    <QuizCreate />
                  </ProtectedRoute>
                }
              />
              <Route path=":quizId">
                <Route
                  path="attempt"
                  element={
                    <ProtectedRoute roles={["STUDENT"]}>
                      <QuizAttempt />
                    </ProtectedRoute>
                  }
                />
                <Route path="results" element={<QuizResults />} />
              </Route>
            </Route>

            {/* Lessons */}
            <Route path="lessons">
              <Route index element={<LessonsList />} />
              <Route
                path=":lessonId"
                element={
                  <ProtectedRoute roles={["STUDENT", "TEACHER", "ADMIN"]}>
                    <LessonDetails />
                  </ProtectedRoute>
                }
              />
              <Route
                path="today"
                element={
                  <ProtectedRoute roles={["STUDENT", "TEACHER", "ADMIN"]}>
                    <TodayLessons />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Attendance */}
            <Route path="attendance">
              <Route
                path="record"
                element={
                  <ProtectedRoute roles={["TEACHER", "ADMIN"]}>
                    <AttendanceRecord />
                  </ProtectedRoute>
                }
              />
              <Route
                path="report"
                element={
                  <ProtectedRoute roles={["TEACHER", "ADMIN"]}>
                    <AttendanceReport />
                  </ProtectedRoute>
                }
              />
              <Route
                path=":lessonId"
                element={
                  <ProtectedRoute roles={["TEACHER", "ADMIN"]}>
                    <AttendanceRecord />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Subjects */}
            <Route path="subjects" element={<SubjectList />} />

            {/* Statistics */}
            <Route path="statistics">
              <Route path="student/:studentId" element={<StudentStats />} />
              <Route
                path="course/:courseId"
                element={
                  <ProtectedRoute roles={["TEACHER", "ADMIN"]}>
                    <CourseStats />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Notifications */}
            <Route path="notifications">
              <Route index element={<NotificationCenter />} />
              <Route
                path="create"
                element={
                  <ProtectedRoute roles={["ADMIN", "TEACHER"]}>
                    <CreateNotification />
                  </ProtectedRoute>
                }
              />
            </Route>

            {/* Materials */}
            <Route path="materials">
              <Route index element={<MaterialsList />} />
              <Route path="upload" element={<MaterialUpload />} />
            </Route>

            <Route path="unauthorized" element={<div>Unauthorized</div>} />
            <Route path="*" element={<div>Not Found</div>} />
          </Route>
        </Routes>
      </AppProvider>
    </AuthProvider>
  );
}

export default App;
