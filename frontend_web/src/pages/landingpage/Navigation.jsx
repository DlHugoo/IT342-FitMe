import React, { useEffect, useRef, useState } from "react";
import FitmeLogo from "../../assets/FitmeLogo.png";
import { useNavigate } from "react-router-dom";

const Navigation = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  // Close dropdown on outside click
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
    window.location.reload();
  };

  return (
    <>
      <header className="sticky top-0 bg-white z-10 shadow-sm">
        <div className="container mx-auto px-6 md:px-36 py-4 flex items-center">
          <div
            className="flex items-center mr-auto cursor-pointer"
            onClick={() => navigate("/")}
          >
            <img src={FitmeLogo} alt="Fitme Logo" className="h-10 w-10 mr-2" />
            <div className="text-3xl font-bold text-fitme-blue mr-auto">
              FITME
            </div>
          </div>

          {/* Navigation - Desktop */}
          <nav className="hidden md:flex space-x-6 mr-6">
            <a
              onClick={() => navigate("/aboutus")}
              className="text-gray-700 hover:text-fitme-blue transition-colors cursor-pointer"
            >
              About Us
            </a>
            <a
              onClick={() => navigate("/download")}
              className="text-gray-700 hover:text-fitme-blue transition-colors cursor-pointer"
            >
              Download
            </a>

            {isLoggedIn ? (
              <div className="relative" ref={dropdownRef}>
                <button
                  onClick={() => setDropdownOpen(!dropdownOpen)}
                  className="text-gray-700 font-semibold cursor-pointer hover:text-fitme-blue"
                >
                  Hi, there!
                </button>

                {dropdownOpen && (
                  <div className="absolute left-0 mt-2 w-25 bg-white border border-gray-200 rounded-lg shadow-lg z-20">
                    <button
                      onClick={handleLogout}
                      className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
                    >
                      Logout
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <a
                onClick={() => navigate("/login")}
                className="text-gray-700 hover:text-fitme-blue transition-colors cursor-pointer"
              >
                Login
              </a>
            )}
          </nav>

          {/* CTA Button */}
          <button
            onClick={() => navigate("/register")}
            className="hidden md:block bg-fitme-blue text-white font-semibold py-2 px-4 rounded-lg hover:bg-fitme-blue-hover transition-colors"
          >
            Try FITME for free
          </button>

          {/* Mobile Menu Button (can be expanded later) */}
          <button className="md:hidden text-gray-700 ml-auto">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>
        </div>

        {/* Mobile Menu (optional - can be made dynamic later) */}
        <div className="hidden md:hidden px-4 py-2 bg-gray-50">
          <a
            onClick={() => navigate("/aboutus")}
            className="block py-2 text-gray-700 hover:text-fitme-blue cursor-pointer"
          >
            About Us
          </a>
          <a
            onClick={() => navigate("/download")}
            className="block py-2 text-gray-700 hover:text-fitme-blue cursor-pointer"
          >
            Download
          </a>
          {!isLoggedIn && (
            <a
              onClick={() => navigate("/login")}
              className="block py-2 text-gray-700 hover:text-fitme-blue cursor-pointer"
            >
              Login
            </a>
          )}
          <button
            onClick={() => navigate("/register")}
            className="mt-3 w-full bg-fitme-blue text-white font-semibold py-2 px-4 rounded-lg hover:bg-fitme-blue-hover transition-colors"
          >
            Try FITME for free
          </button>
        </div>
      </header>
    </>
  );
};

export default Navigation;
