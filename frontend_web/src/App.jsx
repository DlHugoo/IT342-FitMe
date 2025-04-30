import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FitmeLandingPage from "./pages/landingpage/FitmeLandingPage";
import RegisterPage from "./pages/register/RegisterPage";
import LoginPage from "./pages/register/LoginPage";
import DownloadPage from "./pages/downloadpage/DownloadPage";
import AboutUsPage from "./pages/abousus/AboutUsPage";
import UserManagementPage from "./pages/usermngpage/UserManagementPage";
import ExerciseManagementPage from "./pages/exercisemngpage/ExerciseManagementPage";
import WorkoutManagementPage from "./pages/workoutpage/WorkoutManagementPage";
import WorkoutDaysPage from "./pages/workoutpage/WorkoutDaysPage";
import WorkoutDaysExercisePage from "./pages/workoutpage/WorkoutDaysExercisePage";
import AdminRoute from "./route/AdminRoute";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<FitmeLandingPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/download" element={<DownloadPage />} />
        <Route path="/aboutus" element={<AboutUsPage />} />

        {/* 🔒 Admin-only routes */}
        <Route
          path="/user"
          element={
            <AdminRoute>
              <UserManagementPage />
            </AdminRoute>
          }
        />
        <Route
          path="/exercise"
          element={
            <AdminRoute>
              <ExerciseManagementPage />
            </AdminRoute>
          }
        />
        <Route
          path="/workout"
          element={
            <AdminRoute>
              <WorkoutManagementPage />
            </AdminRoute>
          }
        />
        <Route
          path="/workout/:id"
          element={
            <AdminRoute>
              <WorkoutDaysPage />
            </AdminRoute>
          }
        />
        <Route
          path="/workout/:id/days/:dayId"
          element={
            <AdminRoute>
              <WorkoutDaysExercisePage />
            </AdminRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
