import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FitmeLandingPage from "./pages/landingpage/FitmeLandingPage";
import RegisterPage from "./pages/register/RegisterPage";
import LoginPage from "./pages/register/LoginPage";
import DownloadPage from "./pages/downloadpage/DownloadPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<FitmeLandingPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/download" element={<DownloadPage />} />
      </Routes>
    </Router>
  );
}

export default App;
