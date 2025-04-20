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

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<FitmeLandingPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/download" element={<DownloadPage />} />
        <Route path="/aboutus" element={<AboutUsPage />} />
        <Route path="/user" element={<UserManagementPage />} />
        <Route path="/exercise" element={<ExerciseManagementPage />} />
        <Route path="/workout" element={<WorkoutManagementPage />} />
        <Route path="/workout/:id" element={<WorkoutDaysPage />} />
        <Route
          path="/workout/:id/days/:dayId"
          element={<WorkoutDaysExercisePage />}
        />
      </Routes>
    </Router>
  );
}

export default App;
