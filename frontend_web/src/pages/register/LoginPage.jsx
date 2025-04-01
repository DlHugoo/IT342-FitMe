import React, { useState } from "react";
import FitmeLogo from "../../assets/FitmeLogo.png";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const LoginPage = () => {
  const navigate = useNavigate();

  const [formValues, setFormValues] = useState({
    identifier: "", // email or username
    password: "",
  });

  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e) => {
    setFormValues({
      ...formValues,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await axios.post("/api/auth/login", {
        email: formValues.identifier,
        password: formValues.password,
      });

      // Assume the response contains a JWT or user info
      const { token } = response.data;

      // Save token to localStorage or context
      localStorage.setItem("token", token);

      // Navigate to home on success
      navigate("/");
    } catch (err) {
      setError("Invalid credentials. Please try again.");
      console.error("Login error:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center bg-white p-4">
      <div className="mt-8 mb-12">
        <div className="flex items-center mr-auto">
          <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mr-2" />
          <div className="text-3xl font-bold text-fitme-blue mr-auto">
            FITME
          </div>
        </div>
      </div>

      <div className="w-full max-w-md bg-white rounded-lg shadow-lg p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-1">Sign in</h1>
        <p className="text-gray-700 mb-6">Start your fitness journey today</p>

        <form onSubmit={handleLogin}>
          <div className="mb-4">
            <input
              name="email"
              type="text"
              value={formValues.identifier}
              onChange={handleInputChange}
              placeholder="Enter Email"
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>

          <div className="mb-2">
            <input
              name="password"
              type="password"
              value={formValues.password}
              onChange={handleInputChange}
              placeholder="Password"
              className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>

          <div className="mb-6">
            <a href="#" className="text-blue-500 hover:text-blue-600 text-sm">
              Forgot Password?
            </a>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-100 text-red-700 rounded-lg">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-400 hover:bg-blue-500 text-white font-medium py-3 px-4 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {loading ? "Signing In..." : "Sign In"}
          </button>

          <div className="flex items-center my-6">
            <div className="flex-grow h-px bg-gray-300"></div>
            <span className="mx-4 text-gray-500">or</span>
            <div className="flex-grow h-px bg-gray-300"></div>
          </div>

          <button
            type="button"
            className="w-full border border-gray-300 rounded-full py-3 px-4 flex items-center justify-center gap-2 hover:bg-gray-50"
          >
            <svg
              viewBox="0 0 24 24"
              width="18"
              height="18"
              xmlns="http://www.w3.org/2000/svg"
            >
              {/* Google Icon Paths */}
              <g transform="matrix(1, 0, 0, 1, 27.009001, -39.238998)">
                <path
                  fill="#4285F4"
                  d="M -3.264 51.509 C -3.264 50.719 -3.334 49.969 -3.454 49.239 L -14.754 49.239 L -14.754 53.749 L -8.284 53.749 C -8.574 55.229 -9.424 56.479 -10.684 57.329 L -10.684 60.329 L -6.824 60.329 C -4.564 58.239 -3.264 55.159 -3.264 51.509 Z"
                />
                <path
                  fill="#34A853"
                  d="M -14.754 63.239 C -11.514 63.239 -8.804 62.159 -6.824 60.329 L -10.684 57.329 C -11.764 58.049 -13.134 58.489 -14.754 58.489 C -17.884 58.489 -20.534 56.379 -21.484 53.529 L -25.464 53.529 L -25.464 56.619 C -23.494 60.539 -19.444 63.239 -14.754 63.239 Z"
                />
                <path
                  fill="#FBBC05"
                  d="M -21.484 53.529 C -21.734 52.809 -21.864 52.039 -21.864 51.239 C -21.864 50.439 -21.724 49.669 -21.484 48.949 L -21.484 45.859 L -25.464 45.859 C -26.284 47.479 -26.754 49.299 -26.754 51.239 C -26.754 53.179 -26.284 54.999 -25.464 56.619 L -21.484 53.529 Z"
                />
                <path
                  fill="#EA4335"
                  d="M -14.754 43.989 C -12.984 43.989 -11.404 44.599 -10.154 45.789 L -6.734 42.369 C -8.804 40.429 -11.514 39.239 -14.754 39.239 C -19.444 39.239 -23.494 41.939 -25.464 45.859 L -21.484 48.949 C -20.534 46.099 -17.884 43.989 -14.754 43.989 Z"
                />
              </g>
            </svg>
            Sign in with Google
          </button>
        </form>
      </div>

      <div className="mt-8 text-center">
        <p className="text-gray-600">
          New to Fitme?
          <a
            onClick={() => navigate("/register")}
            href="#"
            className="text-blue-500 ml-1 font-medium"
          >
            Join now
          </a>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
